package com.finsight.backend.controller;


import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.common.exception.ErrorCode;
import com.finsight.backend.service.ProductService;
import com.finsight.backend.common.adapter.ProductAdapter;
import com.finsight.backend.common.util.JwtUtil;
import com.finsight.backend.domain.vo.product.ProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductAdapter productAdapter;
    private final JwtUtil jwtUtil;


    @GetMapping("/{category}/{code}")
    public ResponseEntity<?> findDetailProduct(HttpServletRequest request,
                                               @PathVariable("category") String category,
                                               @PathVariable("code") String productCode){
        Class<? extends ProductVO> productType = productAdapter.category(category);
        String userId = jwtUtil.extractUserIdFromRequest(request);

        if (productType == null) {
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(ErrorCode.NOT_PATH_INVALID.getMessage());
        }

        ProductDetailDto productDetailDto = productService.findProduct(productCode, productType, userId);
        return ResponseEntity.ok(productDetailDto);
    }

    @GetMapping("/{category}")
    public ResponseEntity<?> findProductByFilter(HttpServletRequest request,
                                                 @PathVariable("category") String category,
                                                 @RequestParam("sort") String sort,
                                                 @RequestParam(name = "country", required = false) String country,
                                                 @RequestParam(name = "type", required = false) String type,
                                                 @RequestParam(name = "is_matched") Boolean isMatched){
        Class<? extends ProductVO> productCategory = productAdapter.category(category);
        String userId = jwtUtil.extractUserIdFromRequest(request);
        if(productCategory == null){
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(ErrorCode.NOT_PATH_INVALID.getMessage());
        }

        List<? extends ProductByFilterDto> productByFilter = productService.findProductByFilter(productCategory, sort, country, type, userId, isMatched);
        return ResponseEntity.ok(productByFilter);
    }
}
