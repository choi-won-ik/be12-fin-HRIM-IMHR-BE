package com.example.be12hrimimhrbe.domain.product.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // 제품 고유 ID

    @Column(nullable = false)
    private String productName; // 제품명

    private Boolean ecoCertified; // 환경 인증 여부

    private String certificationType; // 인증 종류 (에코마크, GR인증 등)

    private String energyGrade; // 에너지 효율 등급

    private Boolean recyclable; // 재활용 가능 여부

    private Boolean bioMaterial; // 생분해성 소재 여부

    private Boolean lowCarbonProcess; // 탄소 저감형 공정 여부

    private Integer unitPrice; // 단가

    private Integer salesQty; // 판매 수량

    private String imagePath; // 이미지 경로

    private String serialNumber; // 시리얼 넘버

    // 연관된 회사 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    private Company company;

    /**
     * 🛠 제품 정보 수정 (DTO -> Entity)
     * - ProductUpdateReq를 받아 현재 엔티티 필드 업데이트
     */
    public void updateFrom(ProductDto.ProductUpdateReq dto) {
        this.productName = dto.getProductName();
        this.ecoCertified = dto.getEcoCertified();
        this.certificationType = dto.getCertificationType();
        this.energyGrade = dto.getEnergyGrade();
        this.recyclable = dto.getRecyclable();
        this.bioMaterial = dto.getBioMaterial();
        this.lowCarbonProcess = dto.getLowCarbonProcess();
        this.unitPrice = dto.getUnitPrice();
        this.salesQty = dto.getSalesQty();
        this.serialNumber = dto.getSerialNumber();
        // ❗ imagePath, company 변경은 이 메서드에서 제외
    }
}
