package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.domain.score.model.ScoreDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Tag(name = "친환경 제품 관리 기능")
public class ProductController {
    @PostMapping("/detail/{idx}")
    @Operation(summary = "친환경 제품 상세", description = "친환경 제품 상세 페이지 입니다.")
    public ResponseEntity<BaseResponse<ProductDto.ProductDetailResp>> detail(
            @Parameter(description = "제품 IDX", example = "1")
            @PathVariable int idx) {
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,new ProductDto.ProductDetailResp()));
    }
}
