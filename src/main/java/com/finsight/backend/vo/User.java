package com.finsight.backend.vo;

import com.finsight.backend.enumerate.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String userId;

    private String userName;

    private String userPassword;

    private LocalDate userBirthday;

    private String userEmail;

    private LocalDateTime userCreatedAt;

    private UserRole userRole;
}
