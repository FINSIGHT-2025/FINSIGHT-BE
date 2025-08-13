package com.finsight.backend.service;

import com.finsight.backend.repository.mapper.InvTestMapper;
import com.finsight.backend.dto.InvTestDTO;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.common.util.JwtUtil;
import com.finsight.backend.domain.vo.user.InvestmentProfileVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvTestService {

    private final InvTestMapper invTestMapper;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Transactional
    public String getInvestmentProfileTypeByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 투자성향을 조회합니다.");
        String investmentProfileType = invTestMapper
                .selectInvestmentProfileTypeByUserId(userId);
        if (investmentProfileType != null) {
            System.out.println("Service: 투자성향 조회 성공 - " + investmentProfileType);
        } else {
            System.out.println("Service: 사용자 [" + userId + "]의 투자성향 정보를 찾을 수 없습니다.");
        }
        return investmentProfileType;
    }
    public String getInvestmentProfileUpdatedAt(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 갱신일자를 조회합니다.");
        String investmentProfileUpdatedAt = invTestMapper
                .selectInvestmentProfileUpdatedAtByUserId(userId);
        if (investmentProfileUpdatedAt != null) {
            System.out.println("Service: 갱신일자 조회 성공 - " + investmentProfileUpdatedAt);
        } else {
            System.out.println("Service: 사용자[" + userId + "]의 갱신일자 정보를 찾을 수 없습니다.");
        }
        return investmentProfileUpdatedAt;
    }

    @Transactional
    public void upsertInvTest(InvTestDTO invTestDTO, String accessToken) {
        System.out.println("Service: 투자성향을 저장 또는 갱신합니다. DTO: " + invTestDTO);

        String userId;
        try {
            Claims claims = jwtUtil.validateToken(accessToken);
            userId = claims.get("userId", String.class);
            if (userId == null) {
                throw new JwtException("토큰에 userId 정보가 없습니다.");
            }
        } catch (JwtException e) {
            System.err.println("[에러] 토큰 검증 실패: " + e.getMessage());
            throw e;
        }

        InvestmentProfileVO invProfileVO = new InvestmentProfileVO(
                userId,
                invTestDTO.getInvestmentProfileType(),
                LocalDateTime.now()
        );

        int affectedRows = invTestMapper.upsertInvestmentProfile(invProfileVO);
        System.out.println("Service: upsert 처리 완료. 영향받은 행 수: " + affectedRows);

        boolean roleUpdated = userMapper.updateUserRoleToComplete(userId);
        if (roleUpdated) {
            System.out.println("Service: user_role 업데이트 성공");
        } else {
            System.out.println("Service: user_role 업데이트 실패");
        }
    }
}