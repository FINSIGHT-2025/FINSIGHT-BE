package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.WatchListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WatchListMapper {
    List<WatchListDTO> selectWatchListDepositByUserId(String userId);
}
