package org.example.productcatelogservice.clients;

import org.example.productcatelogservice.dtos.FakeStoreProductDto;
import org.example.productcatelogservice.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FakeStoreApiClient {

    private RestTemplateUtil restTemplateUtil;

    @Autowired
    public FakeStoreApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateUtil = new RestTemplateUtil(restTemplateBuilder.build());
    }

    public FakeStoreProductDto getFakeStoreProductById(Long id){
        if (id > 20) {
            throw new IllegalArgumentException("Something went wrong on our side.");
        }
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                this.restTemplateUtil.requestForEntity(
                        HttpMethod.GET,
                        "http://fakestoreapi.com/products/{id}",
                        null,
                        FakeStoreProductDto.class,
                        id
                );

        if(this.restTemplateUtil.isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))){
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public List<FakeStoreProductDto> getAllFakeStoreProducts(){
        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductDtoListResponseEntity =
                this.restTemplateUtil.requestForEntity(
                        HttpMethod.GET,
                        "http://fakestoreapi.com/products",
                        null,
                        FakeStoreProductDto[].class
                );

        if(this.restTemplateUtil.isValidResponse(fakeStoreProductDtoListResponseEntity,
                FakeStoreProductDto[].class, HttpStatusCode.valueOf(200))){
            return Arrays.asList(fakeStoreProductDtoListResponseEntity.getBody());
        }

        return null;
    }

    public FakeStoreProductDto createFakeStoreProduct(FakeStoreProductDto fakeStoreProductDto) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                this.restTemplateUtil.requestForEntity(
                        HttpMethod.POST,
                        "https://fakestoreapi.com/products",
                        fakeStoreProductDto,
                        FakeStoreProductDto.class
                );

        if(this.restTemplateUtil.isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))){
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public FakeStoreProductDto replaceFakeStoreProduct(
            FakeStoreProductDto fakeStoreProductDto, Long id) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                this.restTemplateUtil.requestForEntity(
                        HttpMethod.PUT,
                        "https://fakestoreapi.com/products/{id}",
                        fakeStoreProductDto,
                        FakeStoreProductDto.class,
                        id
                );

        if(this.restTemplateUtil.isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public FakeStoreProductDto removeFakeStoreProduct(Long id) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                this.restTemplateUtil.requestForEntity(
                        HttpMethod.DELETE,
                        "https://fakestoreapi.com/products/{id}",
                        null,
                        FakeStoreProductDto.class,
                        id
                );
        if(this.restTemplateUtil.isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }
}
