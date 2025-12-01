package org.example.productcatelogservice.clients;

import io.micrometer.common.lang.Nullable;
import org.example.productcatelogservice.dtos.FakeStoreProductDto;
import org.example.productcatelogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class FakeStoreApiClient {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreProductDto getFakeStoreProductById(Long id){
        if (id > 20) {
            throw new IllegalArgumentException("Something went wrong on our side.");
        }
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity(
                        HttpMethod.GET,
                        "http://fakestoreapi.com/products/{id}",
                        null,
                        FakeStoreProductDto.class,
                        id
                );

        if(isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))){
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public List<FakeStoreProductDto> getAllFakeStoreProducts(){
        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductDtoListResponseEntity =
                requestForEntity(
                        HttpMethod.GET,
                        "http://fakestoreapi.com/products",
                        null,
                        FakeStoreProductDto[].class
                );

        if(isValidResponse(fakeStoreProductDtoListResponseEntity,
                FakeStoreProductDto[].class, HttpStatusCode.valueOf(200))){
            return Arrays.asList(fakeStoreProductDtoListResponseEntity.getBody());
        }

        return null;
    }

    public FakeStoreProductDto createFakeStoreProduct(FakeStoreProductDto fakeStoreProductDto) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity(
                        HttpMethod.POST,
                        "https://fakestoreapi.com/products",
                        fakeStoreProductDto,
                        FakeStoreProductDto.class
                );

        if(isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(201))){
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public FakeStoreProductDto replaceFakeStoreProduct(
            FakeStoreProductDto fakeStoreProductDto, Long id) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity(
                        HttpMethod.PUT,
                        "https://fakestoreapi.com/products/{id}",
                        fakeStoreProductDto,
                        FakeStoreProductDto.class,
                        id
                );

        if(isValidResponse(fakeStoreProductDtoResponseEntity,
                FakeStoreProductDto.class, HttpStatusCode.valueOf(200))) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    private <T> boolean isValidResponse(ResponseEntity<T> responseEntity, Class<T> responseType, HttpStatusCode happyStatusCode) {
        if(responseEntity.getStatusCode().equals(happyStatusCode)
            && responseEntity.hasBody()){
            return true;
        }
        return false;
    }

    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
}
