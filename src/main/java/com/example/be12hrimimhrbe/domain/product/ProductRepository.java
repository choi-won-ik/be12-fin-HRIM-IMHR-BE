package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * ✅ 회사 ID 기준 제품 전체 조회
     * - 회사와 연관된 모든 제품 반환
     */
    @EntityGraph(attributePaths = "company")
    List<Product> findAllByCompany_Idx(Long companyIdx);

    /**
     * ✅ 제품 ID로 존재 여부 확인
     * - 삭제 전 또는 등록 중 중복 검사 시 활용
     */
    boolean existsByIdx(Long idx);

    /**
     * ✅ 제품 단건 조회 (Optional)
     * - 예외 처리 분리 전략에 적합
     */
    Optional<Product> findByIdx(Long idx);

    // 향후 예시 (조건부 필터 조회를 대비해 템플릿용 메서드 제공 가능)
    // List<Product> findByCompany_IdxAndEcoCertifiedTrueOrderBySalesQtyDesc(Long companyIdx);
}
