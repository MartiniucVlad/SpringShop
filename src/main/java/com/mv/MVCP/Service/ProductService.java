package com.mv.MVCP.Service;

import com.mv.MVCP.dto.ProductDto;
import com.mv.MVCP.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<ProductDto> findAll();

    void insertProduct(ProductDto productDto);

    ProductDto findById(Long id);

    void editProduct(ProductDto productDto);

    void deleteProduct(Long id);

    List<ProductDto> searchProducts(String title);
}
