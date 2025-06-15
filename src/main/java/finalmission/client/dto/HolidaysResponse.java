package finalmission.client.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record HolidaysResponse(
        List<HolidayItem> items,
        int numOfRows,
        int pageNo,
        int totalCount
) {
    private static final DateTimeFormatter holidayDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<LocalDate> getHolidayDates() {
        return this.items().stream()
                .map(item -> LocalDate.parse(item.locdate(), holidayDateFormatter))
                .toList();
    }

    public record HolidayItem (
        String locdate
    ) {
    }
}
