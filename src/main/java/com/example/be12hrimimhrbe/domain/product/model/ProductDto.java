package com.example.be12hrimimhrbe.domain.product.model;

import lombok.*;
import com.example.be12hrimimhrbe.domain.product.model.Product;

public class ProductDto {

    /**
     * ✅ 제품 등록 요청 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductRegistReq {
        private String productName;           // 제품명
        private Boolean ecoCertified;         // 환경 인증 여부
        private String certificationType;     // 인증 종류
        private String energyGrade;           // 에너지 효율 등급
        private Boolean recyclable;           // 재활용 가능 여부
        private Boolean bioMaterial;          // 생분해성 소재 여부
        private Boolean lowCarbonProcess;     // 탄소 저감형 공정 여부
        private Integer unitPrice;            // 단가
        private Integer salesQty;             // 판매 수량
        private Long companyIdx;              // 소속 회사 ID
    }

    /**
     * ✅ 제품 수정 요청 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductUpdateReq {
        private String productName;
        private Boolean ecoCertified;
        private String certificationType;
        private String energyGrade;
        private Boolean recyclable;
        private Boolean bioMaterial;
        private Boolean lowCarbonProcess;
        private Integer unitPrice;
        private Integer salesQty;
    }

    /**
     * ✅ 제품 상세 응답 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetailResp {
        private Long productIdx;
        private String productName;
        private Boolean ecoCertified;
        private String certificationType;
        private String energyGrade;
        private Boolean recyclable;
        private Boolean bioMaterial;
        private Boolean lowCarbonProcess;
        private Integer unitPrice;
        private Integer salesQty;
        private String imagePath;
        private String companyName;

        /**
         * 🔁 Product 엔티티로부터 DTO 변환
         */
        public static ProductDetailResp from(Product product) {
            return ProductDetailResp.builder()
                    .productIdx(product.getIdx())
                    .productName(product.getProductName())
                    .ecoCertified(product.getEcoCertified())
                    .certificationType(product.getCertificationType())
                    .energyGrade(product.getEnergyGrade())
                    .recyclable(product.getRecyclable())
                    .bioMaterial(product.getBioMaterial())
                    .lowCarbonProcess(product.getLowCarbonProcess())
                    .unitPrice(product.getUnitPrice())
                    .salesQty(product.getSalesQty())
                    .imagePath(product.getImagePath())
                    .companyName(product.getCompany().getName())
                    .build();
        }
    }
}
