package org.example.productcatelogservice.repositories;

import org.example.productcatelogservice.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testQueries()
    {
//         List<Product> productList = productRepo.findAll();
//         System.out.println(productList.get(0));

//        List<Product> productList = productRepository.findProductByPriceBetween(1D,900D);
//        System.out.println(productList.size());
//        System.out.println(productList.get(0).getId());
//        System.out.println(productList.get(0).getName());
//
//        List<Product> productList2 = productRepository.findProductByIsPrime(true);
//        System.out.println(productList2.size());
//        System.out.println(productList2.get(0).getId());
//        System.out.println(productList2.get(0).getName());
//
//        List<Product> productList3 = productRepository.findProductByOrderByPriceAsc();
//        System.out.println(productList3.size());
//        System.out.println(productList3.get(0).getName());
//        System.out.println(productList3.get(0).getPrice());

        System.out.println(productRepository.findProductDescriptionById(17L));
    }
}