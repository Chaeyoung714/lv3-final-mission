package finalmission.service;

import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationCreateRequest;
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
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MusicalRepository musicalRepository;
    private final SeatRepository seatRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, MusicalRepository musicalRepository,
                              SeatRepository seatRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.musicalRepository = musicalRepository;
        this.seatRepository = seatRepository;
        this.memberRepository = memberRepository;
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
        authorizeMyReservationRead(myReservation, member);
        return new ReservationFullResponse(myReservation);
    }

    private void authorizeMyReservationRead(Reservation myReservation, Member member) {
        if (!myReservation.matchesMember(member)) {
            throw new UnauthorizedException("자신의 예약만 조회할 수 있습니다.");
        }
    }

    public ReservationSimpleResponse createReservation(
            ReservationCreateRequest request,
            LoginMemberInfo loginMemberInfo
    ) {
        Member member = memberRepository.findById(loginMemberInfo.id())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Musical requestMusical = musicalRepository.findById(request.musicalId())
                .orElseThrow(() -> new NoSuchElementException("뮤지컬 정보를 찾을 수 없습니다."));
        Seat requestSeat = seatRepository.findById(request.seatId())
                .orElseThrow(() -> new NoSuchElementException("좌석 정보를 찾을 수 없습니다."));
        MusicalTime requestMusicalTime = MusicalTime.from(request.musicalTime());

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
}
