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
        Company company = companyRepository.findById(dto.getCompanyIdx())
                .orElseThrow(() -> new IllegalArgumentException("❗ 회사 정보를 찾을 수 없습니다."));

        String imagePath = "http://localhost:8080/img/" + fileService.upload(imageFile);

        Product product = Product.builder()
                .productName(dto.getProductName())
                .ecoCertified(dto.getEcoCertified())
                .certificationType(dto.getCertificationType())
                .energyGrade(dto.getEnergyGrade())
                .recyclable(dto.getRecyclable())
                .bioMaterial(dto.getBioMaterial())
                .lowCarbonProcess(dto.getLowCarbonProcess())
                .unitPrice(dto.getUnitPrice())
                .salesQty(dto.getSalesQty())
                .imagePath(imagePath)
                .company(company)
                .serialNumber(dto.getSerialNumber())
                .build();

        return productRepository.save(product).getIdx();
    }

    /**
     * ✅ 제품 상세 조회
     */
    public ProductDto.ProductDetailResp getDetail(Long idx) {
        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("❗ 해당 제품이 존재하지 않습니다."));
        return ProductDto.ProductDetailResp.from(product);
    }

    /**
     * ✅ 제품 수정
     */
    public Long updateProduct(Long idx, ProductDto.ProductUpdateReq dto) {

        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("❗ 수정할 제품이 존재하지 않습니다."));

        product.setProductName(dto.getProductName());
        product.setEcoCertified(dto.getEcoCertified());
        product.setCertificationType(dto.getCertificationType());
        product.setEnergyGrade(dto.getEnergyGrade());
        product.setRecyclable(dto.getRecyclable());
        product.setBioMaterial(dto.getBioMaterial());
        product.setLowCarbonProcess(dto.getLowCarbonProcess());
        product.setUnitPrice(dto.getUnitPrice());
        product.setSalesQty(dto.getSalesQty());
        product.setSerialNumber(dto.getSerialNumber());

        return productRepository.save(product).getIdx();

    }

    /**
     * ✅ 제품 삭제
     */
    public void deleteProduct(Long idx) {
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
