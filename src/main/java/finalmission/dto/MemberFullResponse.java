package finalmission.dto;

import finalmission.domain.Member;

public record MemberFullResponse(
        Long id,
        String name,
        String email
) {
    public MemberFullResponse(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
