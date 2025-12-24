package com.emis.studentsservice.utils;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class ClientHelper {

    private final WebClient webClient;

    public <R> Mono<R> getRequestWithPathVariables(String url,
                                                   Map<String, String> pathVariables,
                                                   MultiValueMap<String, String> headers,
                                                   Class<R> responseType){
        log.info("Making request to: {}", url);

        return webClient
                .get()
                .uri(url, pathVariables)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(responseType);

    }
    public static MultiValueMap<String, String> getHeaders(){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(1);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
