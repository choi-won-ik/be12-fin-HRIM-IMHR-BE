package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "친환경 제품 관리 기능")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/regist")
    @Operation(summary = "친환경 제품 등록", description = "제품과 이미지를 함께 등록합니다.")
    public ResponseEntity<BaseResponse<Long>> regist(
            @RequestPart("dto") ProductDto.ProductRegistReq dto,
            @RequestPart("image") MultipartFile imageFile
    ) {
        return ResponseEntity.ok(productService.registerProduct(dto, imageFile));
    }
}
