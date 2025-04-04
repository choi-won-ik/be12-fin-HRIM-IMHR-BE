package com.example.be12hrimimhrbe.domain.product.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}