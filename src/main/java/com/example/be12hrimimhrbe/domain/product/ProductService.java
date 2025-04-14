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

    public BaseResponse<Long> registerProduct(ProductDto.ProductRegistReq dto, MultipartFile imageFile) {
        Company company = companyRepository.findById(dto.getCompanyIdx())
                .orElseThrow(() -> new RuntimeException("회사 정보를 찾을 수 없습니다."));

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
                .company(company)
                .img(imagePath)
                .build();

        Product saved = productRepository.save(product);
        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, saved.getIdx());
    }
}
