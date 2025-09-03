package finalmission.service;

import finalmission.client.DataClient;
import finalmission.client.dto.HolidaysResponse;
import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationFullRequest;
import finalmission.dto.ReservationFullResponse;
import finalmission.dto.ReservationSimpleResponse;
import finalmission.domain.Member;
import finalmission.domain.Musical;
import finalmission.domain.MusicalTime;
import finalmission.domain.Reservation;
import finalmission.domain.Seat;
import finalmission.exception.UnauthorizedException;
import finalmission.repository.MemberRepository;
import finalmission.repository.MusicalRepository;
import finalmission.repository.ReservationRepository;
import finalmission.repository.SeatRepository;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MusicalRepository musicalRepository;
    private final SeatRepository seatRepository;
    private final MemberRepository memberRepository;
    private final DataClient dataClient;

    @Autowired
    private EntityManager entityManager;

    public ReservationService(ReservationRepository reservationRepository, MusicalRepository musicalRepository,
                              SeatRepository seatRepository, MemberRepository memberRepository, DataClient dataClient) {
        this.reservationRepository = reservationRepository;
        this.musicalRepository = musicalRepository;
        this.seatRepository = seatRepository;
        this.memberRepository = memberRepository;
        this.dataClient = dataClient;
    }

    public List<ReservationFullResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationFullResponse::new)
                .toList();
    }

    public List<ReservationFullResponse> findMyReservations(LoginMemberInfo loginMemberInfo) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        return reservationRepository.findReservationsByMember(member).stream()
                .map(ReservationFullResponse::new)
                .toList();
    }

    public ReservationFullResponse findMyReservationById(LoginMemberInfo loginMemberInfo, Long reservationId) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Reservation myReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
        authorizeMyReservation(myReservation, member);
        return new ReservationFullResponse(myReservation);
    }

    public ReservationSimpleResponse createReservation(
            ReservationFullRequest request,
            LoginMemberInfo loginMemberInfo
    ) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Musical requestMusical = musicalRepository.findById(request.musicalId())
                .orElseThrow(() -> new NoSuchElementException("뮤지컬 정보를 찾을 수 없습니다."));
        Seat requestSeat = seatRepository.findById(request.seatId())
                .orElseThrow(() -> new NoSuchElementException("좌석 정보를 찾을 수 없습니다."));
        MusicalTime requestMusicalTime = MusicalTime.from(request.musicalTime());

        validateDate(request.date(), requestMusical.getMusicalMonth());
        validateMaxTicketCount(member, requestMusical);
        validateDuplicatedSeat(requestSeat, requestMusical);

        //TODO 검증로직 추가하기
        Reservation createdReservation = reservationRepository.save(new Reservation(
                request.date(),
                requestMusicalTime,
                requestMusical,
                member,
                requestSeat
        ));
        return new ReservationSimpleResponse(createdReservation);
    }

    public ReservationSimpleResponse updateReservation(
            ReservationFullRequest request,
            LoginMemberInfo loginMemberInfo,
            Long reservationId
    ) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Reservation myReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
        authorizeMyReservation(myReservation, member);

        if (request.seatId() != null) {
            Seat requestSeat = seatRepository.findById(request.seatId())
                    .orElseThrow(() -> new NoSuchElementException("좌석 정보를 찾을 수 없습니다."));
            validateDuplicatedSeat(requestSeat, myReservation.getMusical());
            myReservation.changeSeatTo(requestSeat);
        }
        if (request.date() != null
                || request.musicalTime() != null
                || request.musicalId() != null
        ) {
            throw new IllegalArgumentException("극과 날짜 및 시간은 변경할 수 없습니다. 예약 취소 후 재예매해주세요.");
        }

        return new ReservationSimpleResponse(
                reservationRepository.findById(reservationId).get()
        );
    }

    public void deleteMyReservation(LoginMemberInfo loginMemberInfo, Long reservationId) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Reservation myReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
        authorizeMyReservation(myReservation, member);

        reservationRepository.delete(myReservation);
    }

    private void authorizeMyReservation(Reservation myReservation, Member member) {
        if (!myReservation.matchesMember(member)) {
            throw new UnauthorizedException("자신의 예약만 조회 및 변경할 수 있습니다.");
        }
    }

    private void validateDuplicatedSeat(Seat requestSeat, Musical requestMusical) {
        boolean hasDuplicatedSeatReservation = reservationRepository.existsBySeatAndMusical(requestSeat, requestMusical);
        if (hasDuplicatedSeatReservation) {
            throw new IllegalArgumentException("이미 선택된 좌석입니다.");
        }
    }

    private void validateMaxTicketCount(Member member, Musical musical) {
        long reservationsCount = reservationRepository.countReservationsByMemberAndMusical(member, musical);
        if (reservationsCount >= 3) {
            throw new IllegalArgumentException("1인 당 예매는 한 공연 당 최대 3회로 제한됩니다.");
        }
    }

    private void validateDate(LocalDate date, int musicalMonth) {
        if (date.getMonthValue() != musicalMonth) {
            throw new IllegalArgumentException(String.format("해당 뮤지컬은 %d월 예약만 가능합니다.", musicalMonth));
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new IllegalArgumentException("일요일엔 예약 가능한 뮤지컬이 없습니다.");
        }
        HolidaysResponse holidaysResponse = dataClient.getHolidayData(date.getYear(), date.getMonthValue());
        List<LocalDate> holidays = holidaysResponse.getHolidayDates();
        if (holidays.contains(date)) {
            throw new IllegalArgumentException("공휴일엔 예약 가능한 뮤지컬이 없습니다.");
        }
    }
}
