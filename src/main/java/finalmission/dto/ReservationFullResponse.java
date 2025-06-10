package finalmission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.entity.Member;
import finalmission.entity.Musical;
import finalmission.entity.MusicalTime;
import finalmission.entity.Seat;
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
            LocalDate date,
            MusicalTime musicalTime,
            Musical musical,
            Member member,
            Seat seat
    ) {
        this(
                date,
                musicalTime.getTime(),
                new MusicalFullResponse(musical),
                new MemberFullResponse(member),
                new SeatFullResponse(seat)
        );
    }
}
