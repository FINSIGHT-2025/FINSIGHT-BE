package com.finsight.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsSentimentTotalDto {
    private Integer positive;
    private Integer negative;
    private Integer neutral;
}
