package org.example.productcatelogservice.controllers;

import org.example.productcatelogservice.dtos.CategoryDto;
import org.example.productcatelogservice.dtos.ProductDto;
import org.example.productcatelogservice.models.Product;
import org.example.productcatelogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("/products")
    public List<Product> getProducts() {
        return null;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Please pass product id > 0");
        }

        Product product = iProductService.getProductById(productId);
        if(product == null) {
            throw new RuntimeException("Something went wrong");
        }

        ProductDto productDto = from(product);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        return ResponseEntity.ok(productDto);
        return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
    }

    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return null;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto input) {
        return null;
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto input) {
        return null;
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setPrice(product.getPrice());

        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategoryDto(categoryDto);
        }
        return productDto;
    }
}
