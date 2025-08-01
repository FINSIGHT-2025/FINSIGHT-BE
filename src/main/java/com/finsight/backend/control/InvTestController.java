// 2025-07-29 JY
package com.finsight.backend.control;

import com.finsight.backend.dto.InvTestDTO;
import com.finsight.backend.exception.InvTestException;
import com.finsight.backend.service.InvTestService;
import com.finsight.backend.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController // Restful API Controler임을 선언함.
@RequestMapping("/users/invt")// URL 경로 설정
public class InvTestController {
    @Autowired // Service 주입
    private InvTestService invTestService;

    @PutMapping("") // json 형식으로 user_id, risk_type을 받음
    public ResponseEntity<String> insertInvTest(@RequestBody InvTestDTO invTestDTO, HttpServletRequest request) {
        try {

            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new RuntimeException("header 값이 올바르지 않습니다."));

            invTestService.insertInvTest(invTestDTO, accessToken);

            return new ResponseEntity<>("[성공] InvestmentProfile 테이블에 성공적으로 INSERT 되었습니다.",HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("[에러] InvestmentProfile 테이블에 INSERT 도중 에러가 발생했습니다." + e.getMessage());
            // 사용자 정의 예외 던지기
            throw new InvTestException("[실패] InvestmentProfile 테이블에 INSERT가 실패했습니다.", e);
        }
    }
}
