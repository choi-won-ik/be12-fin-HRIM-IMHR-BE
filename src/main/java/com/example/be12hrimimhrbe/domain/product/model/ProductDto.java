package com.example.be12hrimimhrbe.domain.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ProductDto {
    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ProductDetailResp {
        @Schema(description = "상품 이름", example = "1회용 주방 수세미")
        private String productName;
        @Schema(description = "에코 인증", example = "True")
        private Boolean ecoCertified;
        @Schema(description = "인증유형", example = "1회용 주방 수세미")
        private String certificationType;
        @Schema(description = "에너지 등습", example = "1등급")
        private String energyGrade;
        @Schema(description = "재활용 가능 유무", example = "True")
        private Boolean recyclable;
        @Schema(description = "생체 적합 물질", example = "True")
        private Boolean bioMaterial;
        @Schema(description = "저탄소 공정", example = "True")
        private Boolean lowCarbonProcess;
        @Schema(description = "가격", example = "1회용 주방 수세미")
        private Integer unitPrice;
        @Schema(description = "매출 수량", example = "1회용 주방 수세미")
        private Integer salesQty;
        @Schema(description = "제조사", example = "닥터퓨리")
        private String conPanyName;
    }
}
