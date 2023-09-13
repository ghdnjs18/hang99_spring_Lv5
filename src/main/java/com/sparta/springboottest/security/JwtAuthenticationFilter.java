package com.sparta.springboottest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springboottest.dto.LoginRequestDto;
import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.entity.RefreshToken;
import com.sparta.springboottest.entity.UserRoleEnum;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        // 로그인 요청 URI 정의
        setFilterProcessesUrl("/api/user/login");
    }

    // 로그인 토큰 생성
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // 인증관리자를 통해 인증방식 지정해 토큰 생성
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(username, role);
        jwtUtil.addJwtToCookie(accessToken, response);

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username).orElse(null);
        String refresh = jwtUtil.createRefreshToken(username, role);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(refresh, username);
        } else {
            refreshToken.updateToken(refresh);
        }
        refreshTokenRepository.save(refreshToken);
        response.addHeader("Refresh_Token", refreshToken.getToken());

        response.setContentType("application/json; charset=UTF-8");
        MessageResponseDto message = new MessageResponseDto("로그인 성공했습니다.", HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json; charset=UTF-8");
        MessageResponseDto message = new MessageResponseDto("로그인 실패했습니다.", HttpStatus.UNAUTHORIZED.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
