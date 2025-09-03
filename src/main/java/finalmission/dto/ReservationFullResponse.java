package finalmission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationFullResponse(
        LocalDate date,
        @JsonFormat(pattern = "HH:mm") LocalTime musicalTime,
        MusicalFullResponse musical,
        MemberFullResponse member,
        SeatFullResponse seat
) {
    public ReservationFullResponse(
            Reservation reservation
    ) {
        this(
                reservation.getDate(),
                reservation.getMusicalTime().getTime(),
                new MusicalFullResponse(reservation.getMusical()),
                new MemberFullResponse(reservation.getMember()),
                new SeatFullResponse(reservation.getSeat())
        );
    }
}
