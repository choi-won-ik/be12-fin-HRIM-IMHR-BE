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
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, productService.registerProduct(dto, imageFile)));
    }

    @GetMapping("/detail/{idx}")
    @Operation(summary = "제품 상세 조회", description = "제품 정보를 상세히 조회합니다.")
    public ResponseEntity<BaseResponse<ProductDto.ProductDetailResp>> getDetail(@PathVariable Long idx) {
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, productService.getDetail(idx)));
    }

    @PutMapping("/{idx}")
    @Operation(summary = "제품 정보 수정", description = "제품 정보를 수정합니다.")
    public ResponseEntity<BaseResponse<String>> update(
            @PathVariable Long idx,
            @RequestBody ProductDto.ProductUpdateReq dto) {
        productService.updateProduct(idx, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, "수정 완료"));
    }

    @DeleteMapping("/{idx}")
    @Operation(summary = "제품 삭제", description = "제품을 삭제합니다.")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Long idx) {
        productService.deleteProduct(idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, "삭제 완료"));
    }
}
