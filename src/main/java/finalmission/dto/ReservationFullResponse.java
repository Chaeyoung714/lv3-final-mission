package finalmission.dto;

import finalmission.entity.MusicalTime;
import finalmission.entity.Reservation;
import finalmission.entity.SeatGrade;
import java.time.LocalDate;

public record ReservationFullResponse(
        LocalDate date,
        MusicalTime musicalTime,
        String musicalTitle,
        String memberName,
        SeatGrade seatGrade,
        int seatNumber
) {
    public ReservationFullResponse(Reservation createdReservation) {
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
