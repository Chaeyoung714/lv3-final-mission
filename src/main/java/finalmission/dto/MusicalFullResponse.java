package finalmission.dto;

import finalmission.entity.Musical;

public record MusicalFullResponse(
        Long id,
        String title,
        String description
) {
    public MusicalFullResponse(Musical musical) {
        this(
                musical.getId(),
                musical.getTitle(),
                musical.getDescription()
        );
    }
}
