package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.LocalImageService;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocalImageService localImageService;

    // ✅ 상품 등록
//    @Transactional
//    public BaseResponse<Product> register(ProductDto.RegistReq dto, MultipartFile imgFile) {
//        String imageUrl = localImageService.upload(imgFile);
//
//        Product product = Product.builder()
//                .name(dto.getName())
//                .serial(dto.getSerial())
//                .description(dto.getDescription())
//                .imageUrl(imageUrl)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        return new BaseResponse<>(BaseResponseMessage.PRODUCT_CREATED, productRepository.save(product));
//    }
//
//    // ✅ 상품 전체 조회
//    public BaseResponse<List<ProductDto.Response>> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        List<ProductDto.Response> response = products.stream()
//                .map(ProductDto.Response::from)
//                .toList();
//
//        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, response);
//    }
//
//    // ✅ 상품 상세 조회
//    public BaseResponse<ProductDto.Response> getProductDetail(Long idx) {
//        Product product = productRepository.findById(idx)
//                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
//
//        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, ProductDto.Response.from(product));
//    }
//
//    // ✅ 상품 수정
//    @Transactional
//    public BaseResponse<Product> update(Long idx, ProductDto.UpdateReq dto, MultipartFile imgFile) {
//        Product product = productRepository.findById(idx)
//                .orElseThrow(() -> new RuntimeException("수정할 상품이 존재하지 않습니다."));
//
//        String imageUrl = imgFile != null ? localImageService.upload(imgFile) : product.getImageUrl();
//
//        product.update(
//                dto.getName(),
//                dto.getSerial(),
//                dto.getDescription(),
//                imageUrl
//        );
//
//        return new BaseResponse<>(BaseResponseMessage.PRODUCT_UPDATED, product);
//    }
//
//    // ✅ 상품 삭제
//    @Transactional
//    public BaseResponseMessage delete(Long idx) {
//        Product product = productRepository.findById(idx)
//                .orElseThrow(() -> new RuntimeException("삭제할 상품이 존재하지 않습니다."));
//
//        productRepository.delete(product);
//        return BaseResponseMessage.PRODUCT_DELETED;
//    }
}
