package com.finsight.backend.control;


import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.service.ProductService;
import com.finsight.backend.adapter.ProductAdapter;
import com.finsight.backend.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;


    @GetMapping("/{category}/{code}")
    public ResponseEntity<?> findDetailProduct(@PathVariable("category") String category,
                                               @PathVariable("code") String productCode){
        Class<? extends Product> productType = ProductAdapter.productType(category);
        Class<? extends ProductDetailDto> productDetailType = ProductAdapter.productDetailDtoType(category);

        if (productType == null) {
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_PATH_INVALID.getMessage()));
        }

        ProductDetailDto productDetailDto = productService.findProduct(productCode, productType, productDetailType);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE, productDetailDto, null));
    }
}
