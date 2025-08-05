// 2025-07-29 JY
package com.finsight.backend.dto;

import com.finsight.backend.enumerate.InvTestProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data // Getter, Setter, equals, hashCode, toString 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
@Builder // 빌더 패턴 자동 생성
public class InvTestDTO {
    private InvTestProfileType investmentProfileType;
//    private investmentProfileUpdatedAt investmentProfileUpdatedAt;
}