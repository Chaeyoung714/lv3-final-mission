package finalmission.repository;

import finalmission.domain.Member;
import finalmission.domain.Musical;
import finalmission.domain.Reservation;
import finalmission.domain.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationsByMember(Member member);

    long countReservationsByMemberAndMusical(Member member, Musical musical);

    boolean existsBySeatAndMusical(Seat seat, Musical musical);
}
