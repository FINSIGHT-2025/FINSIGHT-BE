package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String userId;
    private String username;
    private String nickname;
    private String email;
    private Role role;
}
