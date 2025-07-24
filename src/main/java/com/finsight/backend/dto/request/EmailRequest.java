package com.finsight.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

/*
클라이언트(프론트엔드)에서 백엔드로 이메일 관련 요청을 보낼 때 사용하는 데이터 객체
이메일 인증 요청 시 email을 받아오기 위해 사용
인증번호 확인 요청 시 email과 code를 함께 받아오기 위해 사용
 */
@Getter
@Setter
public class EmailRequest {
    private String email;
}
