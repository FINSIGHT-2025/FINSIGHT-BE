/*
2025-07-29 JY
[비즈니스 로직]
o FE에서 받은 user_id, risk_type과 BE에서 생성한 update_at을 받아서,
o DTO에 담겨있는 정보를 Mapper를 통해 DB에 INSERT를 함.
 */
package com.finsight.backend.service;

import com.finsight.backend.mapper.InvTestMapper;
import com.finsight.backend.dto.InvTestDTO;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.util.JwtUtil;
import com.finsight.backend.vo.InvTestVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class InvTestService {

    private final InvTestMapper invTestMapper;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Transactional // DB 트랜잭션 관리가 필요한 경우에 사용되는 @
    public void insertInvTest(InvTestDTO invTestDTO, String accessToken) {
        System.out.println("Service: 사용자의 투자성향을 INSERT 합니다 ... " + invTestDTO.toString());

        String userId;
        try {
            // ✅ JWT 토큰 검증 및 userId 추출 (보안 강화)
            Claims claims = jwtUtil.validateToken(accessToken);
            userId = claims.get("userId", String.class); // JwtUtil에서 userId 클레임 이름 확인
            if (userId == null) {
                throw new JwtException("토큰에 userId 정보가 없습니다.");
            }
        } catch (JwtException e) {
            System.err.println("[에러] 토큰 검증 실패: " + e.getMessage());
            throw e; // 컨트롤러에서 InvTestException으로 변환하여 처리
        }
        InvTestVO invTestVO = new InvTestVO(
                userId,
                invTestDTO.getInvestmentProfileType(),
                now()
        );

        // ✅ 투자성향 INSERT
        invTestMapper.insert(invTestVO);
        System.out.println("Service: 사용자의 투자성향이 성공적으로 INSERT 되었습니다.");

        // ✅ user_role UPDATE
        boolean result = userMapper.updateUserRoleToComplete(userId);
        if (result) {
            System.out.println("Service: user_role 업데이트 성공");
        } else {
            System.out.println("Service: user_role 업데이트 실패");
        }
    }

    public String getInvestmentProfileTypeByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 투자성향을 조회합니다.");
        // InvTestMapper를 통해 DB에서 투자 성향 유형을 조회합니다.
        // 이 메서드는 Mapper에 새롭게 추가될 select 쿼리를 호출합니다.
        String investmentProfileType = invTestMapper
                .selectInvestmentProfileTypeByUserId(userId);

        if (investmentProfileType != null) {
            System.out.println("Service: 투자성향 조회 성공 - " + investmentProfileType);
        } else {
            System.out.println("Service: 사용자 [" + userId + "]의 투자성향 정보를 찾을 수 없습니다.");
        }
        return investmentProfileType;
    }
}
