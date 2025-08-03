package com.finsight.backend.mapper;

import com.finsight.backend.vo.Etf;

public interface EtfMapper {
    Etf findEtfByCode(String productCode);
}
