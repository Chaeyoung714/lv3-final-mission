package finalmission.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import finalmission.client.DataClient;
import finalmission.client.dto.HolidaysResponse;
import finalmission.client.dto.HolidaysResponse.HolidayItem;
import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationFullRequest;
import finalmission.entity.Member;
import finalmission.entity.Musical;
import finalmission.entity.Seat;
import finalmission.entity.SeatGrade;
import finalmission.repository.MemberRepository;
import finalmission.repository.MusicalRepository;
import finalmission.repository.ReservationRepository;
import finalmission.repository.SeatRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MusicalRepository musicalRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DataClient dataClient;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member("moda", "moda@woowa.com", "1234")));
        when(musicalRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Musical(Month.of(5), "memphis", "my favorite!!")));
        when(seatRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Seat(SeatGrade.VIP, 1)));
    }

    @DisplayName("예약 생성 서비스 실행 시 공휴일 검증을 한다.")
    @Test
    void createReservationAndValidateHolidayDateTest() {
        LocalDate holiday = LocalDate.of(2025, 5, 5);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                holiday,
                LocalTime.of(14, 30),
                1L,
                1L
        );

        when(dataClient.getHolidayData(any(Integer.class), any(Integer.class)))
                .thenReturn(new HolidaysResponse(
                        List.of(new HolidayItem("20250505"), new HolidayItem("20250506")),
                        10, 1, 3
                ));

        assertThatThrownBy(
                () -> reservationService.createReservation(reservationRequest, new LoginMemberInfo(1L, "moda"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 서비스 실행 시 일요일 검증을 한다.")
    @Test
    void createReservationAndValidateSundayDateTest() {
        LocalDate sunday = LocalDate.of(2025, 5, 4);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                sunday,
                LocalTime.of(14, 30),
                1L,
                1L
        );

        assertThatThrownBy(
                () -> reservationService.createReservation(reservationRequest, new LoginMemberInfo(1L, "moda"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 서비스 실행 시 뮤지컬의 월과 동일한지 검증을 한다.")
    @Test
    void createReservationAndValidateMusicalMonthTest() {
        LocalDate otherMonthDate = LocalDate.of(2025, 4, 4);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                otherMonthDate,
                LocalTime.of(14, 30),
                1L,
                1L
        );

        assertThatThrownBy(
                () -> reservationService.createReservation(reservationRequest, new LoginMemberInfo(1L, "moda"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 서비스 실행 시 1인당 최대 3회 예매했는지 검증을 한다.")
    @Test
    void createReservationAndValidateTicketLimitTest() {
        LocalDate date = LocalDate.of(2025, 5, 4);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                date,
                LocalTime.of(14, 30),
                1L,
                1L
        );

        when(reservationRepository.countReservationsByMemberAndMusical(any(Member.class), any(Musical.class)))
                .thenReturn(3L);

        assertThatThrownBy(
                () -> reservationService.createReservation(reservationRequest, new LoginMemberInfo(1L, "moda"))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}