package com.example.be12hrimimhrbe.domain.product.model;

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
    }
}
