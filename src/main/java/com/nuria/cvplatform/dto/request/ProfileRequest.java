package com.nuria.cvplatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequest {

        @NotBlank
        private String fullName;
        @NotBlank
        private String title;
        @NotBlank
        @Size(max = 2000)
        private String summary;
        @NotBlank
        @Email(message = "Invalid email format")
        private String email;
        private String phone;
        private String linkedin;
        private String github;
}
