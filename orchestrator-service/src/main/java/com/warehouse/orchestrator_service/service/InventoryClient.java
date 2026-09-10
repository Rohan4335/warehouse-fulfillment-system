package com.warehouse.orchestrator_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final RestClient restClient;

    public boolean checkStock(String productCode, int quantity) {
        Boolean result = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory/{productCode}/check")
                        .queryParam("quantity", quantity)
                        .build(productCode))
                .retrieve()
                .body(Boolean.class);

        return Boolean.TRUE.equals(result);
    }

    public void reserveStock(String productCode, int quantity) {
        restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory/{productCode}/reserve")
                        .queryParam("quantity", quantity)
                        .build(productCode))
                .retrieve()
                .toBodilessEntity();
    }

    public void releaseStock(String productCode, int quantity) {
        restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory/{productCode}/release")
                        .queryParam("quantity", quantity)
                        .build(productCode))
                .retrieve()
                .toBodilessEntity();
    }
}