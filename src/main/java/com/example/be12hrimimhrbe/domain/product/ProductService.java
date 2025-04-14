package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.product.model.Product;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import com.example.be12hrimimhrbe.global.utils.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final FileService fileService;

    public Long registerProduct(ProductDto.ProductRegistReq dto, MultipartFile imageFile) {
        Company company = companyRepository.findById(dto.getCompanyIdx())
                .orElseThrow(() -> new IllegalArgumentException("회사 없음"));

        String imagePath = fileService.upload(imageFile);

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
                .build();

        return productRepository.save(product).getIdx();
    }

    public ProductDto.ProductDetailResp getDetail(Long idx) {
        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("제품 없음"));
        return ProductDto.ProductDetailResp.from(product);
    }

    public void updateProduct(Long idx, ProductDto.ProductUpdateReq dto) {
        Product product = productRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("제품 없음"));

        product.setProductName(dto.getProductName());
        product.setEcoCertified(dto.getEcoCertified());
        product.setCertificationType(dto.getCertificationType());
        product.setEnergyGrade(dto.getEnergyGrade());
        product.setRecyclable(dto.getRecyclable());
        product.setBioMaterial(dto.getBioMaterial());
        product.setLowCarbonProcess(dto.getLowCarbonProcess());
        product.setUnitPrice(dto.getUnitPrice());
        product.setSalesQty(dto.getSalesQty());
    }

    public void deleteProduct(Long idx) {
        productRepository.deleteById(idx);
    }
}
