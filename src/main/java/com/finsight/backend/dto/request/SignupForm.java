package com.finsight.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {

    @NotBlank @Size(min = 4, max = 20)
    private String userId;

    @NotBlank @Size(min = 10)
    private String password;

    @NotBlank @Size(min = 2, max = 20)
    private String username;

    @NotBlank @Size(min = 2, max = 10)
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull @Past
    private LocalDate birthday;

    @NotBlank @Email
    private String email;
}
