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

    private String userId;

    private String username;

    private String nickname;

    private String password;

    private LocalDate birthday;

    private String email;

    private LocalDateTime createdAt;

    private Role role;
}
