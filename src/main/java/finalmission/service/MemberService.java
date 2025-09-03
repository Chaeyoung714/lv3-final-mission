package finalmission.service;

import finalmission.auth.JwtTokenProvider;
import finalmission.dto.LoginRequest;
import finalmission.domain.Member;
import finalmission.repository.MemberRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String publishToken(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new NoSuchElementException("이메일 혹은 비밀번호가 일치하지 않습니다."));
        return jwtTokenProvider.createToken(member);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }
}
