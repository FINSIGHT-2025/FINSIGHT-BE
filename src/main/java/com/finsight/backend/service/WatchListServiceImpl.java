package com.finsight.backend.service;

import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.repository.mapper.WatchListMapper;
import org.springframework.stereotype.Service;

@Service
public abstract class WatchListServiceImpl implements WatchListService {
    private final WatchListMapper watchListMapper;

    public WatchListServiceImpl(WatchListMapper watchListMapper) {
        this.watchListMapper = watchListMapper;
    }

    // 찜한 상품 등록
    @Override
    public int insertWatch(WatchListDTO watchListDTO) {
        return watchListMapper.insertWatch(watchListDTO);
    }

    // 찜한 상품 해제
    @Override
    public int deleteWatch(String userId, String productCode, String productCategory) {
        return watchListMapper.deleteWatch(userId, productCode, productCategory);
    }
    
}
