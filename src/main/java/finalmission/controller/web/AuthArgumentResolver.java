package finalmission.controller.web;

import finalmission.auth.JwtTokenProvider;
import finalmission.dto.LoginMemberInfo;
import finalmission.dto.annotation.CurrentMember;
import finalmission.domain.Member;
import finalmission.exception.UnauthorizedException;
import finalmission.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class)
                && parameter.getParameterType().equals(LoginMemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractCookie(nativeRequest);
        Long id = jwtTokenProvider.extractId(token);
        Member member = findExistingMemberById(id);
        return new LoginMemberInfo(member);
    }

    private String extractCookie(HttpServletRequest nativeRequest) {
        Cookie[] cookies = nativeRequest.getCookies();
        if (cookies == null) {
            throw new UnauthorizedException("토큰이 존재하지 않습니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new UnauthorizedException("토큰이 존재하지 않습니다.");
    }

    private Member findExistingMemberById(Long id) {
        try {
            return memberService.findMemberById(id);
        } catch (NoSuchElementException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
}
