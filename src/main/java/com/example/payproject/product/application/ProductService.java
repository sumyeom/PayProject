package com.example.payproject.product.application;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.product.application.dto.ProductCommand;
import com.example.payproject.product.application.dto.ProductInfo;
import com.example.payproject.product.domain.Product;
import com.example.payproject.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<List<ProductInfo>> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductInfo> products = page.stream()
                .map(ProductInfo::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), products, page.getTotalElements());
    }

    public ResponseEntity<ProductInfo> create(ProductCommand command) {
        UUID operator = command.operatorId() != null ? command.operatorId() : UUID.randomUUID();
        Product product = Product.create(
                command.sellerId(),
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                operator
        );
        Product saved = productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), ProductInfo.from(saved), 1);
    }

    public ResponseEntity<ProductInfo> update(UUID productId, ProductCommand command) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        UUID operator = command.operatorId() != null ? command.operatorId() : product.getModifyId();
        product.update(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                operator
        );
        Product updated = productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.OK.value(), ProductInfo.from(updated), 1);
    }

    public ResponseEntity<Void> delete(UUID productId) {
        productRepository.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), null, 0);
    }
}