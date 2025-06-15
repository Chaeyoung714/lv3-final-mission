package finalmission.service;

import finalmission.client.DataClient;
import finalmission.client.dto.HolidaysResponse;
import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationFullRequest;
import finalmission.dto.ReservationFullResponse;
import finalmission.dto.ReservationSimpleResponse;
import finalmission.entity.Member;
import finalmission.entity.Musical;
import finalmission.entity.MusicalTime;
import finalmission.entity.Reservation;
import finalmission.entity.Seat;
import finalmission.exception.UnauthorizedException;
import finalmission.repository.MemberRepository;
import finalmission.repository.MusicalRepository;
import finalmission.repository.ReservationRepository;
import finalmission.repository.SeatRepository;
import jakarta.persistence.EntityManager;
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
        validateDateHoliday(request.date());

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

    private void validateDateHoliday(LocalDate date) {
        HolidaysResponse holidaysResponse = dataClient.getHolidayData(date.getYear(), date.getDayOfMonth());
        List<LocalDate> holidays = holidaysResponse.getHolidayDates();
        if (holidays.contains(date)) {
            throw new IllegalArgumentException("공휴일엔 예약할 수 없습니다.");
        }
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
}
