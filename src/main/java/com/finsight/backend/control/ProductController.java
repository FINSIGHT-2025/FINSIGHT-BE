package com.finsight.backend.control;


import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.ProductByFilterDto;
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
    private final ProductAdapter productAdapter;


    @GetMapping("/{category}/{code}")
    public ResponseEntity<?> findDetailProduct(@PathVariable("category") String category,
                                               @PathVariable("code") String productCode){
        Class<? extends Product> productType = productAdapter.productType(category);

        if (productType == null) {
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(ErrorCode.NOT_PATH_INVALID.getMessage());
        }

        ProductDetailDto productDetailDto = productService.findProduct(productCode, productType);
        return ResponseEntity.ok(productDetailDto);
    }

    @GetMapping("/{category}")
    public ResponseEntity<?> findProductByFilter(@PathVariable("category") String category,
                                                 @RequestParam("sort") String sort,
                                                 @RequestParam(name = "country", required = false) String country,
                                                 @RequestParam(name = "type", required = false) String type,
                                                 @RequestParam(name = "riskGrade", required = false) Integer riskGrade,
                                                 @RequestParam(name = "limit") Integer limit,
                                                 @RequestParam(name = "offset") Integer offset){
        Class<? extends Product> productType = productAdapter.productType(category);

        if(productType == null){
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(ErrorCode.NOT_PATH_INVALID.getMessage());
        }

        List<? extends ProductByFilterDto> productByFilter = productService.findProductByFilter(productType, sort, country, type, riskGrade, limit, offset);
        return ResponseEntity.ok(productByFilter);
    }
}
