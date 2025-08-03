package com.finsight.backend.dto.response;

import com.finsight.backend.vo.Fund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundByFilterDto extends ProductByFilterDto{
    String country;
    String fundType;
    Double rateOfReturn;
    Integer scale;
    Integer riskGrade;
    List<Map<String, String>> newsResponse;


}
