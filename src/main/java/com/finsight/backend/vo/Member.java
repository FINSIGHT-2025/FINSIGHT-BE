package com.finsight.backend.vo;

import com.finsight.backend.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @NotBlank @Size(min = 4, max = 20)
    private String userId;

    @NotBlank @Size(max = 20)
    private String username;

    @NotBlank @Size(min = 2, max = 20)
    private String nickname;

    @NotBlank
    private String password;

    @NotNull @Past
    private LocalDate birthday;

    @NotBlank @Email
    private String email;

    @NotNull @Past
    private LocalDateTime createdAt;

    @NotNull
    private Role role;
}
