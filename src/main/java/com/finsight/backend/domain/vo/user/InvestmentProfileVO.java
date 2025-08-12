// 2025-07-30 JY
package com.finsight.backend.domain.vo.user;

import com.finsight.backend.domain.enumerate.InvestmentProfileType;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentProfileVO {
    private String userId;

    private InvestmentProfileType investmentProfileType;

    private LocalDateTime investmentProfileUpdatedAt;

}
