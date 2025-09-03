package finalmission.dto;

import finalmission.domain.MusicalTime;
import finalmission.domain.Reservation;
import finalmission.domain.SeatGrade;
import java.time.LocalDate;

public record ReservationSimpleResponse(
        LocalDate date,
        MusicalTime musicalTime,
        String musicalTitle,
        String memberName,
        SeatGrade seatGrade,
        int seatNumber
) {
    public ReservationSimpleResponse(Reservation createdReservation) {
        this(
                createdReservation.getDate(),
                createdReservation.getMusicalTime(),
                createdReservation.getMusical().getTitle(),
                createdReservation.getMember().getName(),
                createdReservation.getSeat().getGrade(),
                createdReservation.getSeat().getNumber()
        );
    }
}
