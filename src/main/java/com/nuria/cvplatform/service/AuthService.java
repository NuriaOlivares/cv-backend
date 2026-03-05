package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.LoginRequest;
import com.nuria.cvplatform.dto.response.LoginResponse;
import com.nuria.cvplatform.model.RefreshToken;
import com.nuria.cvplatform.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtService.generateToken(userDetails.getUsername(), role);
        addAccessTokenCookie(response, accessToken);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        addRefreshTokenCookie(response, refreshToken.getToken());

        log.info("User {} logged in", userDetails.getUsername());

        return LoginResponse.builder()
                .userId(userDetails.getUsername())
                .role(role)
                .build();
    }

    @Transactional
    public void refresh(String refreshToken, HttpServletResponse response) {

        RefreshToken verified = refreshTokenService.verifyToken(refreshToken);

        refreshTokenService.revokeToken(refreshToken);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(verified.getUserId());
        addRefreshTokenCookie(response, newRefreshToken.getToken());

        String newAccessToken = jwtService.generateToken(verified.getUserId(), "");
        addAccessTokenCookie(response, newAccessToken);
    }

    @Transactional
    public void logout(String userId, HttpServletResponse response) {
        refreshTokenService.revokeAllUserTokens(userId);
        clearCookies(response);
        log.info("User {} logged out", userId);
    }

    private void addAccessTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie access = new Cookie("access_token", "");
        access.setMaxAge(0);
        access.setPath("/");
        response.addCookie(access);

        Cookie refresh = new Cookie("refresh_token", "");
        refresh.setMaxAge(0);
        refresh.setPath("/api/auth/refresh");
        response.addCookie(refresh);
    }
}