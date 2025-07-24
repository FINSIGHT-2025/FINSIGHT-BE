package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDto {
    private String userId;
    private String username;
    private String nickname;
}
