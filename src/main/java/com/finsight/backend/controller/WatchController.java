package com.finsight.backend.controller;

import com.finsight.backend.common.adapter.ProductAdapter;
import com.finsight.backend.domain.vo.product.ProductVO;
import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.service.AuthService;
import com.finsight.backend.service.WatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/watches")
public class WatchController {
    private final WatchService watchService;
    private final AuthService authService;
    private final ProductAdapter productAdapter;

    @PostMapping("")
    public ResponseEntity<String> insertWatch(@RequestBody WatchListDTO watchListDTO, HttpServletRequest request) {
        String userId = authService.isValidToken(request);
        watchService.insertWatch(watchListDTO, userId);

        return ResponseEntity.ok("찜한 상품 둥록 완료");
    }
    @DeleteMapping("/{category}/{code}")
    public ResponseEntity<String> deleteWatch(@PathVariable String category,
                                              @PathVariable String code,
                                              HttpServletRequest request) {
        String userId = authService.isValidToken(request);
        Class<? extends ProductVO> expectedType = productAdapter.category(category);
        watchService.deleteWatch(expectedType, code, userId);

        return ResponseEntity.ok("찜한 상품 해제 완료");
    }

    @GetMapping("/deposit")
    public ResponseEntity<?> watchDepositListByCategory(HttpServletRequest request){
        String userId = authService.isValidToken(request);

        return ResponseEntity.ok(watchService.getWatchDepositListByUserId(userId));
    }

    @GetMapping("/fund")
    public ResponseEntity<?> watchFundListByCategory(HttpServletRequest request){
        String userId = authService.isValidToken(request);

        return ResponseEntity.ok(watchService.getWatchFundListByUserId(userId));
    }

    @GetMapping("/etf")
    public ResponseEntity<?> watchEtfListByCategory(HttpServletRequest request){
        String userId = authService.isValidToken(request);

        return ResponseEntity.ok(watchService.getWatchEtfListByUserId(userId));
    }

}
