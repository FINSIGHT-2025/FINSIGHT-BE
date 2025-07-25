package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.Role;

public record LoginResponseWithToken(
        String email,
        Role role,
        String accessToken
) {}
