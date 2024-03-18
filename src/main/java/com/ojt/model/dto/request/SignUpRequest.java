package com.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignUpRequest {
    @NotBlank(message = "username can't be empty")
    private String username;
    @NotBlank(message = "password not blank")

    private String password;
    @NotBlank(message = "confirmPassword not blank")

    private String confirmPassword;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "email invalid")
    @NotBlank(message = "email not blank")
    private String email;
    private Set<String> roles;
}
