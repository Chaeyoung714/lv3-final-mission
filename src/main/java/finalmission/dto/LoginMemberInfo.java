package finalmission.dto;

import finalmission.domain.Member;

public record LoginMemberInfo(
        Long id,
        String name
) {
    public LoginMemberInfo(Member member) {
        this(
                member.getId(),
                member.getName()
        );
    }
}
