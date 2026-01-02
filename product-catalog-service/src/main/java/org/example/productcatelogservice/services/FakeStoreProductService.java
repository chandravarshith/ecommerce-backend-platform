package org.example.productcatelogservice.services;

import org.example.productcatelogservice.clients.FakeStoreApiClient;
import org.example.productcatelogservice.dtos.CategoryDto;
import org.example.productcatelogservice.dtos.FakeStoreProductDto;
import org.example.productcatelogservice.dtos.ProductDto;
import org.example.productcatelogservice.models.Category;
import org.example.productcatelogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements IProductService {

    @Autowired
    private FakeStoreApiClient fakeStoreApiClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String fakeStoreProductKey = "FAKE_STORE_PRODUCTS";

    @Override
    public Product getProductById(Long id) {
        FakeStoreProductDto fakeStoreProductDto = null;

        fakeStoreProductDto = (FakeStoreProductDto) this.redisTemplate.opsForHash()
                .get(fakeStoreProductKey, id);

        if(fakeStoreProductDto == null){
            fakeStoreProductDto = fakeStoreApiClient.getFakeStoreProductById(id);
            if(fakeStoreProductDto != null) {
                this.redisTemplate.opsForHash().put(fakeStoreProductKey, id, fakeStoreProductDto);
                System.out.println("Fetched from Fake Store");
            }
            else{
                return null;
            }
        }
        else{
            System.out.println("Found in Redis cache");
        }

        return from(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        List<FakeStoreProductDto> fakeStoreProductDtoList = fakeStoreApiClient.getAllFakeStoreProducts();
        if(fakeStoreProductDtoList != null) {
            for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtoList) {
                products.add(from(fakeStoreProductDto));
            }
            return products;
        }
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto inputFakeStoreProductDto = from(product);
        FakeStoreProductDto fakeStoreProductDto =
                fakeStoreApiClient.createFakeStoreProduct(inputFakeStoreProductDto);
        if(fakeStoreProductDto != null) {
            return from(fakeStoreProductDto);
        }
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDto inputFakeStoreProductDto = from(product);
        FakeStoreProductDto fakeStoreProductDto =
                fakeStoreApiClient.replaceFakeStoreProduct(inputFakeStoreProductDto, id);
        if(fakeStoreProductDto != null) {
            return from(fakeStoreProductDto);
        }
        return null;
    }

    @Override
    public Product removeProduct(Long id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreApiClient.removeFakeStoreProduct(id);
        if(fakeStoreProductDto != null) {
            return from(fakeStoreProductDto);
        }
        return null;
    }

    @Override
    public Product getDetailsBasedOnUserRole(Long productId, Long userId) {
        return null;
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        if(fakeStoreProductDto.getCategory() != null) {
            Category category = new Category();
            category.setName(fakeStoreProductDto.getCategory());
            product.setCategory(category);
        }
        return product;
    }

    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if (product.getCategory() != null) {
            Category category = new Category();
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
