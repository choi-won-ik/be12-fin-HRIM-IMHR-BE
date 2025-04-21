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

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "친환경 제품 관리 기능", description = "제품 등록, 조회, 수정, 삭제, 회사별 목록 제공")
public class ProductController {

    private final ProductService productService;

    /**
     * ✅ 제품 등록
     * - JSON DTO (`ProductRegistReq`)
     * - 이미지 파일 (Multipart)
     */
    @PostMapping("/regist")
    @Operation(summary = "제품 등록", description = "제품 정보와 이미지를 함께 등록합니다.")
    public ResponseEntity<BaseResponse<Long>> regist(
            @RequestPart("dto") ProductDto.ProductRegistReq dto,
            @RequestPart("image") MultipartFile imageFile
    ) {
        Long savedId = productService.registerProduct(dto, imageFile);
        return ResponseEntity.ok(
                new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, savedId)
        );
    }

    /**
     * ✅ 제품 상세 조회
     */
    @GetMapping("/detail/{idx}")
    @Operation(summary = "제품 상세 조회", description = "제품 ID로 상세 정보를 조회합니다.")
    public ResponseEntity<BaseResponse<ProductDto.ProductDetailResp>> getDetail(@PathVariable Long idx) {
        ProductDto.ProductDetailResp detail = productService.getDetail(idx);
        return ResponseEntity.ok(
                new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, detail)
        );
    }

    /**
     * ✅ 제품 정보 수정
     * - JSON 본문 수정 (이미지는 제외)
     */
    @PutMapping("/edit/{idx}")
    @Operation(summary = "제품 정보 수정", description = "제품 ID로 기존 정보를 수정합니다.")
    public ResponseEntity<BaseResponse<String>> update(
            @PathVariable Long idx,
            @RequestBody ProductDto.ProductUpdateReq dto
    ) {
        productService.updateProduct(idx, dto);
        return ResponseEntity.ok(
                new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, "수정 완료")
        );
    }

    /**
     * ✅ 제품 삭제
     */
    @DeleteMapping("/delete/{idx}")
    @Operation(summary = "제품 삭제", description = "제품 ID로 삭제합니다.")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Long idx) {
        productService.deleteProduct(idx);
        return ResponseEntity.ok(
                new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, "삭제 완료")
        );
    }

    /**
     * ✅ 회사별 제품 리스트 조회
     */
    @GetMapping("/company/{companyIdx}")
    @Operation(summary = "회사별 제품 리스트", description = "회사 ID로 해당 회사의 모든 제품을 조회합니다.")
    public ResponseEntity<BaseResponse<List<ProductDto.ProductDetailResp>>> getByCompany(
            @PathVariable Long companyIdx
    ) {
        List<ProductDto.ProductDetailResp> productList = productService.getProductsByCompany(companyIdx);
        return ResponseEntity.ok(
                new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, productList)
        );
    }
}
