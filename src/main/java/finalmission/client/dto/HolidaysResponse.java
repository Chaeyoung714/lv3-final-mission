package finalmission.client.dto;

import java.util.List;

public record HolidaysResponse(
        List<HolidayResponse> items
) {

    public record HolidayResponse (
        String locdate
    ) {
    }
}
