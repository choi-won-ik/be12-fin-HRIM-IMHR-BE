package com.example.be12hrimimhrbe.domain.product.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ProductDto {

    /**
     * ✅ 제품 등록 요청 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "제품 등록 요청 DTO")
    public static class ProductRegistReq {

        @Schema(description = "제품명", example = "에코 텀블러")
        private String productName;

        @Schema(description = "환경 인증 여부", example = "true")
        private Boolean ecoCertified;

        @Schema(description = "인증 종류", example = "GR 인증")
        private String certificationType;

        @Schema(description = "에너지 효율 등급", example = "1등급")
        private String energyGrade;

        @Schema(description = "재활용 가능 여부", example = "true")
        private Boolean recyclable;

        @Schema(description = "생분해성 소재 여부", example = "false")
        private Boolean bioMaterial;

        @Schema(description = "탄소 저감형 공정 여부", example = "true")
        private Boolean lowCarbonProcess;

        @Schema(description = "단가", example = "12000")
        private Integer unitPrice;

        @Schema(description = "판매 수량", example = "100")
        private Integer salesQty;

        @Schema(description = "회사 ID", example = "3")
        private Long companyIdx;

        @Schema(description = "시리얼 넘버", example = "EC-001")
        private String serialNumber;

        @Schema(description = "이미지 경로", hidden = true)
        private String imagePath;

        public Product toEntity(Company company, String imagePath) {
            return Product.builder()
                    .productName(productName)
                    .ecoCertified(ecoCertified)
                    .certificationType(certificationType)
                    .energyGrade(energyGrade)
                    .recyclable(recyclable)
                    .bioMaterial(bioMaterial)
                    .lowCarbonProcess(lowCarbonProcess)
                    .unitPrice(unitPrice)
                    .salesQty(salesQty)
                    .serialNumber(serialNumber)
                    .imagePath(imagePath)
                    .company(company)
                    .build();
        }
    }

    /**
     * ✅ 제품 수정 요청 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "제품 수정 요청 DTO")
    public static class ProductUpdateReq {

        @Schema(description = "제품명", example = "수정된 텀블러")
        private String productName;

        @Schema(description = "환경 인증 여부", example = "false")
        private Boolean ecoCertified;

        @Schema(description = "인증 종류", example = "GRS")
        private String certificationType;

        @Schema(description = "에너지 효율 등급", example = "2등급")
        private String energyGrade;

        @Schema(description = "재활용 가능 여부", example = "false")
        private Boolean recyclable;

        @Schema(description = "생분해성 소재 여부", example = "true")
        private Boolean bioMaterial;

        @Schema(description = "탄소 저감형 공정 여부", example = "false")
        private Boolean lowCarbonProcess;

        @Schema(description = "단가", example = "9500")
        private Integer unitPrice;

        @Schema(description = "판매 수량", example = "200")
        private Integer salesQty;

        @Schema(description = "시리얼 넘버", example = "EC-987654")
        private String serialNumber;

        @Schema(description = "이미지 경로 (기존 이미지 유지 시 사용)", example = "/img/original.jpg", hidden = true)
        private String imagePath; // 선택 사항
    }

    /**
     * ✅ 제품 상세 응답 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "제품 상세 응답 DTO")
    public static class ProductDetailResp {

        @Schema(description = "제품 ID", example = "1")
        private Long idx;

        @Schema(description = "제품명", example = "에코 텀블러")
        private String productName;

        @Schema(description = "환경 인증 여부", example = "true")
        private Boolean ecoCertified;

        @Schema(description = "인증 종류", example = "GR 인증")
        private String certificationType;

        @Schema(description = "에너지 효율 등급", example = "1등급")
        private String energyGrade;

        @Schema(description = "재활용 가능 여부", example = "true")
        private Boolean recyclable;

        @Schema(description = "생분해성 소재 여부", example = "false")
        private Boolean bioMaterial;

        @Schema(description = "탄소 저감형 공정 여부", example = "true")
        private Boolean lowCarbonProcess;

        @Schema(description = "단가", example = "12000")
        private Integer unitPrice;

        @Schema(description = "판매 수량", example = "100")
        private Integer salesQty;

        @Schema(description = "이미지 경로", example = "/img/eco-cup.png")
        private String imagePath;

        @Schema(description = "회사 ID", example = "3")
        private Long companyIdx;

        @Schema(description = "시리얼 넘버", example = "EC-123456")
        private String serialNumber;

        @Schema(description = "전월 대비 증가율", example = "12.5%")
        private String growthRate;

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
                    .serialNumber(product.getSerialNumber())
                    .build();
        }
    }
}
