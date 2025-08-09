package com.finsight.backend.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InvUtil {

    private Map<String, Integer[]> riskGradeMap = Map.of(
            "stable", new Integer[]{6},
            "stableplus", new Integer[]{6, 5},
            "neutral", new Integer[]{6, 5, 4},
            "aggressive", new Integer[]{6, 5, 4, 3, 2},
            "veryaggressive", new Integer[]{6, 5, 4, 3, 2, 1}
    );
    public Integer[] riskGradeRange(String invTestProfileType){
        return riskGradeMap.get(invTestProfileType);
    }
}
