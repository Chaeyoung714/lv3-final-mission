package finalmission.client.dto;

import java.util.List;

public record HolidaysResponse(
        List<HolidayItem> items,
        int numOfRows,
        int pageNo,
        int totalCount
) {

    public record HolidayItem (
        String locdate
    ) {
    }
}
