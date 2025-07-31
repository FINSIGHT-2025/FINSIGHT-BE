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

    @Transactional // DB 트랜잭션 관리가 필요한 경우에 사용되는 @
    public void insertInvTest(InvTestDTO invTestDTO, String accessToken ) {
        System.out.println("Service: 사용자의 risk_type을 INSERT 합니다 ... " + invTestDTO.toString());

        String userId = (String) jwtUtil.decodeToken(accessToken).get("userId");

        InvTestVO invTestVO = new InvTestVO(
                userId,
                invTestDTO.getInvestmentProfileType(),
                now()
        );
        invTestMapper.insert(invTestVO);
        System.out.println("Service: 사용자의 risk_type이 성공적으로 INSERT 되었습니다.");
    }
}
