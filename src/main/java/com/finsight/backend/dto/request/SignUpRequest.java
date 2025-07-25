package com.finsight.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * 회원가입 요청 DTO
 * - 프론트에서 입력받는 필드만 포함
 * - createdAt, role 등 서버에서 처리되는 값은 제외
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String userId;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 20)
    private String nickname;

    @NotBlank
    private String password;

    @NotNull
    @Past
    private LocalDate birthday;

    @NotBlank
    @Email
    private String email;
}

