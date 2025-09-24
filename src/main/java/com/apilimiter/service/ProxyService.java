package com.apilimiter.service;

import com.apilimiter.dto.ProxyDto;
import com.apilimiter.model.ApiRequest;
import com.apilimiter.model.User;
import com.apilimiter.repository.ApiRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.Map;

@Service
public class ProxyService {

    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @Autowired
    private RateLimitService rateLimitService;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ProxyService() {
        this.webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
            .build();
        this.objectMapper = new ObjectMapper();
    }

    public ProxyDto.ProxyResponse forwardRequest(User user, ProxyDto.ProxyRequest request) {
        long startTime = System.currentTimeMillis();

        // Check rate limit
        if (!rateLimitService.isWithinLimit(user)) {
            ProxyDto.ProxyResponse response = new ProxyDto.ProxyResponse();
            response.setError("Rate limit exceeded");
            response.setStatusCode(429);
            response.setRemainingQuota(rateLimitService.getRemainingQuota(user));
            return response;
        }

        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, String> headerMap = objectMapper.readValue(request.getHeaders(), Map.class);
                    headerMap.forEach(headers::set);
                } catch (Exception e) {
                    // Ignore header parsing errors
                }
            }

            // Make the request
            Mono<ResponseEntity<String>> responseMono = webClient
                .method(HttpMethod.valueOf(request.getMethod().toUpperCase()))
                .uri(request.getUrl())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(request.getBody() != null ? request.getBody() : "")
                .retrieve()
                .toEntity(String.class)
                .timeout(Duration.ofSeconds(30));

            ResponseEntity<String> responseEntity = responseMono.block();
            long latency = System.currentTimeMillis() - startTime;

            // Increment request count
            rateLimitService.incrementAndGetCount(user);

            // Log request
            logRequest(user, request.getUrl(), latency, true, 
                      responseEntity != null ? responseEntity.getStatusCode().value() : 200, null);

            // Build response
            ProxyDto.ProxyResponse response = new ProxyDto.ProxyResponse();
            response.setData(parseResponseData(responseEntity != null ? responseEntity.getBody() : ""));
            response.setStatusCode(responseEntity != null ? responseEntity.getStatusCode().value() : 200);
            response.setLatency(latency);
            response.setRemainingQuota(rateLimitService.getRemainingQuota(user));

            return response;

        } catch (WebClientResponseException e) {
            long latency = System.currentTimeMillis() - startTime;

            // Increment request count even for errors
            rateLimitService.incrementAndGetCount(user);

            // Log failed request
            logRequest(user, request.getUrl(), latency, false, e.getStatusCode().value(), e.getMessage());

            // Build error response
            ProxyDto.ProxyResponse response = new ProxyDto.ProxyResponse();
            response.setError(e.getMessage());
            response.setStatusCode(e.getStatusCode().value());
            response.setLatency(latency);
            response.setRemainingQuota(rateLimitService.getRemainingQuota(user));

            return response;

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;

            // Increment request count even for errors
            rateLimitService.incrementAndGetCount(user);

            // Log failed request
            logRequest(user, request.getUrl(), latency, false, 500, e.getMessage());

            // Build error response
            ProxyDto.ProxyResponse response = new ProxyDto.ProxyResponse();
            response.setError("Request failed: " + e.getMessage());
            response.setStatusCode(500);
            response.setLatency(latency);
            response.setRemainingQuota(rateLimitService.getRemainingQuota(user));

            return response;
        }
    }

    private void logRequest(User user, String endpoint, Long latency, Boolean success, Integer statusCode, String error) {
        ApiRequest apiRequest = new ApiRequest(user, endpoint, latency, success, statusCode, error);
        apiRequestRepository.save(apiRequest);
    }

    private Object parseResponseData(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return "";
        }

        try {
            return objectMapper.readValue(responseBody, Object.class);
        } catch (Exception e) {
            return responseBody;
        }
    }
}