package finalmission.entity;

import java.time.LocalTime;
import java.util.Arrays;

public enum MusicalTime {
    AFTERNOON(LocalTime.of(14,30)),
    EVENING(LocalTime.of(17, 30)),
    ;

    private final LocalTime time;

    MusicalTime(LocalTime time) {
        this.time = time;
    }

    public static MusicalTime from(LocalTime time) {
        return Arrays.stream(MusicalTime.values())
                .filter(musicalTime -> musicalTime.time.equals(time))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("정해진 시간대만 예약 가능합니다."));
    }

}
