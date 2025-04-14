package com.example.be12hrimimhrbe.domain.product.model;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import lombok.*;

public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductRegistReq {
        private String productName;
        private Boolean ecoCertified;
        private String certificationType;
        private String energyGrade;
        private Boolean recyclable;
        private Boolean bioMaterial;
        private Boolean lowCarbonProcess;
        private Integer unitPrice;
        private Integer salesQty;
        private Long companyIdx;
        private String img;
    }

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
