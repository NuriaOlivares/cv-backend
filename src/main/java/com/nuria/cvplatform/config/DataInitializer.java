package com.nuria.cvplatform.config;

import com.nuria.cvplatform.model.User;
import com.nuria.cvplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    @Value("${app.admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User admin = User.builder()
                    .userId("admin_nuria")
                    .password(passwordEncoder.encode(adminPassword))
                    .role("ROLE_ADMIN")
                    .build();

            User viewer = User.builder()
                    .userId("viewer_recruiter")
                    .password(passwordEncoder.encode("viewer123"))
                    .role("ROLE_VIEWER")
                    .build();

            userRepository.saveAll(List.of(admin, viewer));
            log.info("✅ Dev users initialized");
        }
    }
}