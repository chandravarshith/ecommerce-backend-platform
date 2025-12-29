package org.example.productcatelogservice.services;

import org.example.productcatelogservice.models.Product;
import org.example.productcatelogservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StorageProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        if(optionalProduct.isEmpty()) return null;
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        Optional<Product> optional = productRepository.findById(product.getId());
        if (optional.isPresent()) {
            return optional.get();
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        if(!id.equals(product.getId()) || optionalProduct.isEmpty()) return null;
        return this.productRepository.save(product);
    }
}
