package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.product.model.Product;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.utils.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final FileService fileService;

    /**
     * ✅ 제품 등록
     */
    public Long registerProduct(ProductDto.ProductRegistReq dto, MultipartFile imageFile) {
        // 회사 조회
        System.out.println("🔥 받은 dto: " + dto);
        System.out.println("🔥 받은 companyIdx: " + dto.getCompanyIdx());
        Company company = companyRepository.findById(dto.getCompanyIdx())
                .orElseThrow(() -> new IllegalArgumentException("❗ 회사 정보를 찾을 수 없습니다."));

        // 이미지 저장 및 경로 생성
        String fileName = fileService.upload(imageFile);
        String imagePath = "http://localhost:8080/img/" + fileName;

        // DTO → Entity 변환
        Product product = dto.toEntity(company, imagePath);

        // 저장 및 ID 반환
        return productRepository.save(product).getIdx();
    }

    /**
     * ✅ 제품 상세 조회
     */
    public ProductDto.ProductDetailResp getDetail(Long idx) {
        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("❗ 해당 제품이 존재하지 않습니다."));
        System.out.println("🔥 받은 dto: " + product.getIdx());
        System.out.println("🔥 받은 idx: " + idx);
        return ProductDto.ProductDetailResp.from(product);
    }

    /**
     * ✅ 제품 수정
     */
    public Long updateProduct(Long idx, ProductDto.ProductUpdateReq dto) {
        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("❗ 수정할 제품이 존재하지 않습니다."));
        System.out.println("🔥 받은 product.idx: " + product.getIdx());
        System.out.println("🔥 받은 idx: " + idx);
        System.out.println("🔥 받은 dto: " + dto);
        product.updateFrom(dto); // Entity 내부 updateFrom 사용
        System.out.println("🔥 받은 product.idx: " + product.getIdx());
        System.out.println("🔥 받은 idx: " + idx);
        return productRepository.save(product).getIdx();
    }

    /**
     * ✅ 제품 삭제
     */
    public void deleteProduct(Long idx) {
        if (!productRepository.existsById(idx)) {
            throw new IllegalArgumentException("❗ 삭제할 제품이 존재하지 않습니다.");
        }
        productRepository.deleteById(idx);
    }

    /**
     * ✅ 회사별 제품 리스트 조회
     */
    public List<ProductDto.ProductDetailResp> getProductsByCompany(Long companyIdx) {
        List<Product> products = productRepository.findByCompany_Idx(companyIdx);
        return products.stream()
                .map(ProductDto.ProductDetailResp::from)
                .collect(Collectors.toList());
    }
}
