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
        private Long idx;                     //제품 idx
        private String productName;           // 제품명
        private Boolean ecoCertified;         // 환경 인증 여부
        private String certificationType;     // 인증 종류
        private String energyGrade;           // 에너지 효율 등급
        private Boolean recyclable;           // 재활용 가능 여부
        private Boolean bioMaterial;          // 생분해성 소재 여부
        private Boolean lowCarbonProcess;     // 탄소 저감형 공정 여부
        private Integer unitPrice;            // 단가
        private Integer salesQty;             // 판매 수량
        private String imagePath;            //이미지 경로
        private Long companyIdx;              // 소속 회사 ID
        private String serialNumber;          //시리얼 넘버 추가
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
        private Long idx;                     //제품 idx
        private String productName;           // 제품명
        private Boolean ecoCertified;         // 환경 인증 여부
        private String certificationType;     // 인증 종류
        private String energyGrade;           // 에너지 효율 등급
        private Boolean recyclable;           // 재활용 가능 여부
        private Boolean bioMaterial;          // 생분해성 소재 여부
        private Boolean lowCarbonProcess;     // 탄소 저감형 공정 여부
        private Integer unitPrice;            // 단가
        private Integer salesQty;             // 판매 수량
        private String imagePath;            //이미지 경로
        private Long companyIdx;              // 소속 회사 ID
        private String serialNumber;          //시리얼 넘버 추가
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
        private Long idx;                     //제품 idx
        private String productName;           // 제품명
        private Boolean ecoCertified;         // 환경 인증 여부
        private String certificationType;     // 인증 종류
        private String energyGrade;           // 에너지 효율 등급
        private Boolean recyclable;           // 재활용 가능 여부
        private Boolean bioMaterial;          // 생분해성 소재 여부
        private Boolean lowCarbonProcess;     // 탄소 저감형 공정 여부
        private Integer unitPrice;            // 단가
        private Integer salesQty;             // 판매 수량
        private String imagePath;            //이미지 경로
        private Long companyIdx;              // 소속 회사 ID
        private String serialNumber;          //시리얼 넘버 추가
        private String growthRate;	          //증가율

        /**
         * 🔁 Product 엔티티로부터 DTO 변환
         */
        public static ProductDetailResp from(Product product) {
            return ProductDetailResp.builder()
                    .idx(product.getIdx())
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
                    .companyIdx(product.getCompany().getIdx())
                    .serialNumber(product.getSerialNumber())//DTO로 변환
                    .build();
        }
    }
}
