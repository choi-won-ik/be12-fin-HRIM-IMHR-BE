package com.example.be12hrimimhrbe.global.config.filter;

import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.time.Duration;

//@RequiredArgsConstructor
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
//    private final AuthenticationManager authenticationManager;

    public LoginFilter(AntPathRequestMatcher antPathRequestMatcher, AuthenticationManager authenticationManager) {
        super(antPathRequestMatcher, authenticationManager);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        if(failed.getMessage().equals("No value present") || failed instanceof BadCredentialsException) {

        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter 실행됐다.");
        UsernamePasswordAuthenticationToken authToken;
//        UserDto.SignupRequest userDto =
//                new UserDto.SignupRequest(request.getParameter("username"), request.getParameter("password"));
        try {
//            MemberDto.PersonalSignupRequest memberDto  = new ObjectMapper().readValue(request.getInputStream(), MemberDto.PersonalSignupRequest.class);
            MemberDto.LoginRequest memberDto = new ObjectMapper().readValue(request.getInputStream(), MemberDto.LoginRequest.class);

            authToken =
                    new UsernamePasswordAuthenticationToken(memberDto.getMemberId()+"_"+memberDto.getWay(), memberDto.getPassword(), null);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        return authenticationManager.authenticate(authToken);
        return this.getAuthenticationManager().authenticate(authToken);
    }


    // 그림에서 9번 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails cud = (CustomUserDetails) authResult.getPrincipal();
        Member member = cud.getMember();
        String jwtToken = JwtUtil.generateToken(member.getIdx(), member.getEmail(),
                member.getMemberId(),
                member.getName(), cud.getHrAuthoritySet(), member.getIsAdmin(),
                member.getHasProdAuth(), member.getHasPartnerAuth(),
                member.getCompany(), member.getDepartment(), member.getStatus()
        );


//        일반적인 객체 생성 및 객체의 변수에 값을 설정하는 방법
//        ResponseCookie cookie = new ResponseCookie();
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);

//        빌더 패턴으로 객체를 생성하면서 값을 설정하는 방법
        ResponseCookie cookie = ResponseCookie
                .from("ATOKEN", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofHours(1L))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

