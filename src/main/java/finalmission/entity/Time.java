package finalmission.entity;

import java.time.LocalTime;

public enum Time {
    AFTERNOON(LocalTime.of(14,30)),
    EVENING(LocalTime.of(17, 30)),
    ;

    private final LocalTime time;

    Time(LocalTime time) {
        this.time = time;
    }
}
