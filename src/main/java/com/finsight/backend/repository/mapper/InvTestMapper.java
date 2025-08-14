package com.finsight.backend.repository.mapper;

import com.finsight.backend.domain.vo.user.InvestmentProfileVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface InvTestMapper {
    String selectInvestmentProfileTypeByUserId(String userId);
    String selectInvestmentProfileUpdatedAtByUserId(String userId);
    int upsertInvestmentProfile(InvestmentProfileVO invProfile);
}
