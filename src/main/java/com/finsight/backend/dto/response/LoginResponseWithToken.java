package com.finsight.backend.dto.response;

import com.finsight.backend.domain.enumerate.UserRole;

public record LoginResponseWithToken(
        String email,
        UserRole role,
        String accessToken
) {}
