package finalmission.dto;

import finalmission.entity.Seat;

public record SeatFullResponse(
        Long id,
        String seatGrade,
        int seatNumber
) {
    public SeatFullResponse(Seat seat) {
        this(
                seat.getId(),
                seat.getGrade().name(),
                seat.getNumber()
        );
    }
}
