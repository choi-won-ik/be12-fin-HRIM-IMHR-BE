package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * ✅ 특정 회사(companyIdx)에 속한 제품 목록 조회
     */
    List<Product> findByCompany_Idx(Long companyIdx);
}
