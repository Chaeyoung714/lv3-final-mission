package finalmission.repository;

import finalmission.entity.Member;
import finalmission.entity.Musical;
import finalmission.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationsByMember(Member member);

    long countReservationsByMemberAndMusical(Member member, Musical musical);
}
