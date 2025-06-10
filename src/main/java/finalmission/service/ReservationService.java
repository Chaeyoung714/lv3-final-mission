package finalmission.service;

import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationCreateRequest;
import finalmission.dto.ReservationFullResponse;
import finalmission.entity.Member;
import finalmission.entity.Musical;
import finalmission.entity.MusicalTime;
import finalmission.entity.Reservation;
import finalmission.entity.Seat;
import finalmission.repository.MemberRepository;
import finalmission.repository.MusicalRepository;
import finalmission.repository.ReservationRepository;
import finalmission.repository.SeatRepository;
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

    public ReservationFullResponse createReservation(
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
        return new ReservationFullResponse(createdReservation);
    }
}
