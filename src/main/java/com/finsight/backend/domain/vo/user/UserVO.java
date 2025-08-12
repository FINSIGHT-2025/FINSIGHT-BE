package com.finsight.backend.domain.vo.user;

import com.finsight.backend.domain.enumerate.UserRole;
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
public class UserVO {

    private String userId;

    private String userName;

    private String userPassword;

    private LocalDate userBirthday;

    private String userEmail;

    private LocalDateTime userCreatedAt;

    private UserRole userRole;
}
