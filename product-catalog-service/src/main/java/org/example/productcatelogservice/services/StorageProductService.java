package org.example.productcatelogservice.services;

import org.example.productcatelogservice.dtos.UserDto;
import org.example.productcatelogservice.models.Product;
import org.example.productcatelogservice.repositories.ProductRepository;
import org.example.productcatelogservice.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
//@Primary
public class StorageProductService implements IProductService {

    private ProductRepository productRepository;

    private RestTemplateUtil restTemplateUtil;

    @Autowired
    public StorageProductService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplateUtil = new RestTemplateUtil(restTemplate);
    }

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
        if(!id.equals(product.getId()) || optionalProduct.isEmpty()) {
            return null;
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product removeProduct(Long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            return null;
        }
        this.productRepository.delete(optionalProduct.get());
        return optionalProduct.get();
    }

    @Override
    public Product getDetailsBasedOnUserRole(Long productId, Long userId) {
        Optional<Product> optionalProduct=productRepository.findById(productId);
        if(optionalProduct.isEmpty()) return null;

        ResponseEntity<UserDto> userDtoResponseEntity = this.restTemplateUtil.requestForEntity(
                HttpMethod.GET,
                "http://user-authentication-service/users/{userId}",
                null,
                UserDto.class,
                userId
        );

        if(!this.restTemplateUtil.isValidResponse(userDtoResponseEntity,
                UserDto.class, HttpStatusCode.valueOf(200))){
            return null;
        }

        System.out.println(userDtoResponseEntity.getBody().getEmail());
        return optionalProduct.get();
    }
}
