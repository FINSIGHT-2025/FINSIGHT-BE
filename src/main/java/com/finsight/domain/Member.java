package com.finsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @NotBlank
    String userId;

    @NotEmpty
    String username;

    @NotEmpty
    String nickname;

    @NotEmpty
    String password;

    @NotNull
    LocalDate birthday;

    @Email
    @NotBlank
    String email;

    LocalDateTime createdAt;
}
