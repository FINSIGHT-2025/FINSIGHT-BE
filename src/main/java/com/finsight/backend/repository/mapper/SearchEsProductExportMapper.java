package com.finsight.backend.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface SearchEsProductExportMapper {
    List<Map<String, Object>> selectDepositsForEs();
    List<Map<String, Object>> selectFundsForEs();
    List<Map<String, Object>> selectEtfsForEs();
}
