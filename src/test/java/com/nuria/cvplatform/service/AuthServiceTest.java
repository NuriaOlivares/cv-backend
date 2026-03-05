package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.LoginRequest;
import com.nuria.cvplatform.dto.response.LoginResponse;
import com.nuria.cvplatform.model.RefreshToken;
import com.nuria.cvplatform.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUserId("admin_nuria");
        request.setPassword("password");

        User userDetails = new User(
                "admin_nuria",
                "encoded_password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(jwtService.generateToken(any(), any())).thenReturn("mocked_jwt_token");
        when(refreshTokenService.createRefreshToken(any())).thenReturn(
                RefreshToken.builder()
                        .token("mocked_refresh_token")
                        .userId("admin_nuria")
                        .expiryDate(LocalDateTime.now().plusDays(7))
                        .revoked(false)
                        .build()
        );

        LoginResponse response = authService.login(request, httpServletResponse);

        assertThat(response.getUserId()).isEqualTo("admin_nuria");
        assertThat(response.getRole()).isEqualTo("ROLE_ADMIN");
        verify(jwtService).generateToken("admin_nuria", "ROLE_ADMIN");
        verify(refreshTokenService).createRefreshToken("admin_nuria");
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest();
        request.setUserId("admin_nuria");
        request.setPassword("wrong_password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authService.login(request, httpServletResponse))
                .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void logout_shouldRevokeTokensAndClearCookies() {
        authService.logout("admin_nuria", httpServletResponse);

        verify(refreshTokenService).revokeAllUserTokens("admin_nuria");

        verify(httpServletResponse, times(2)).addCookie(any());
    }
}
