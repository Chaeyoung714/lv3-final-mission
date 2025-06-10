package finalmission.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateRequest(
        LocalDate date,
        LocalTime musicalTime,
        Long musicalId,
        Long seatId
) {
}
