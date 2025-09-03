package finalmission.client.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record HolidaysResponse(
        HolidayResponse response
) {

        private static final DateTimeFormatter holidayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        public List<LocalDate> getHolidayDates() {
                return response.body().items().item().stream()
                        .map(holidayItem -> LocalDate.parse(holidayItem.locdate(), holidayFormatter))
                        .toList();
        }

        public record HolidayResponse(
                HolidayBody body
        ) {
        }

        public record HolidayBody (
                HolidayItems items,
                int numOfRows,
                int pageNo,
                int totalCount
        ) {
        }

        public record HolidayItems (
                List<HolidayItem> item
        ) {
        }

        public record HolidayItem (
                String locdate
        ) {
        }
}
