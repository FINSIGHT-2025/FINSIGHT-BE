//2025-07-30 JY
package com.finsight.backend.repository.mapper;

import com.finsight.backend.domain.vo.user.InvestmentProfileVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper // MyBatis Mapper 인터페이스임을 선언함.
public interface InvTestMapper {
    void insert(InvestmentProfileVO invTestVO);
    String selectInvestmentProfileTypeByUserId(String userId);
    String selectInvestmentProfileUpdatedAtByUserId(String userId);
}
