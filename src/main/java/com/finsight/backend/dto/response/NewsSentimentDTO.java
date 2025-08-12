package com.finsight.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsSentimentDTO {
    private int positive;
    private int neutral;
    private int negative;
    private int total;
}
