package com.mv.MVCP.Service.impl;

import com.mv.MVCP.Service.ProductService;
import com.mv.MVCP.dto.ProductDto;
import com.mv.MVCP.models.Product;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.ProductRepository;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toProductDto).toList();
    }

    @Override
    public void insertProduct(ProductDto productDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        productDto.setCreatedBy(user);
        productRepository.save(toProduct(productDto));
    }

    @Override
    public ProductDto findById(Long id) {
         Optional<Product> product = productRepository.findById(id);
         if(product.isPresent()) {
             return toProductDto(product.get());
         }
         else {
             return null;
         }
    }

    @Override
    public void editProduct(ProductDto productDto) {

    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> searchProducts(String title) {
        List<Product> products = productRepository.searchProducts(title);
        return products.stream().map(this::toProductDto).toList();
    }


    private ProductDto toProductDto(Product product) {
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .photoUrl(product.getPhotoUrl())
                .content(product.getContent())
                .createdBy(product.getCreatedBy())
                .build();

        return productDto;
    }

    private Product toProduct(ProductDto productDto) {
        Product product = Product.builder()
                .title(productDto.getTitle())
                .photoUrl(productDto.getPhotoUrl())
                .content(productDto.getContent())
                .createdBy(productDto.getCreatedBy())
                .build();
        return product;
    }


}
