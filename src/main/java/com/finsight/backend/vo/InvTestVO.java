// 2025-07-30 JY
package com.finsight.backend.vo;

import com.finsight.backend.enumerate.InvTestProfileType;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvTestVO {
    private String userId;

    private InvTestProfileType investmentProfileType;

    private LocalDateTime investmentProfileUpdatedAt;

}
