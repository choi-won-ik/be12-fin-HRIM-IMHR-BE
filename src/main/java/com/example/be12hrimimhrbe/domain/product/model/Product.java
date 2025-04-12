package com.example.be12hrimimhrbe.domain.product.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String productName;
    private Boolean ecoCertified;
    private String certificationType;
    private String energyGrade;
    private Boolean recyclable;
    private Boolean bioMaterial;
    private Boolean lowCarbonProcess;
    private Integer unitPrice;
    private Integer salesQty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx")
    private Company company;
}
