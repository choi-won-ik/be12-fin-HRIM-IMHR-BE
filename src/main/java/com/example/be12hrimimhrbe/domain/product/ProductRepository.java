package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * ✅ 회사 ID로 제품 리스트 조회
     * - Company 엔티티의 ID 기준
     */
    List<Product> findByCompany_Idx(Long companyIdx);

    /**
     * ✅ 제품 존재 여부 확인
     * - 삭제 전 유효성 검증용
     */
    boolean existsByIdx(Long idx);
}
