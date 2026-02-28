package com.emis.studentsservice.service.client;

import com.emis.studentsservice.config.StudentConfigurationProperties;
import com.emis.studentsservice.dto.response.SchoolDetailsResponse;
import com.emis.studentsservice.exception.SchoolNotFoundException;
import com.emis.studentsservice.exception.SchoolServiceUnavailableException;
import com.emis.studentsservice.utils.ClientHelper;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    private final ClientHelper client;
    private final StudentConfigurationProperties properties;

    @Override
    public Mono<SchoolDetailsResponse> getSchoolDetails(String schoolCode) {
        var url = properties.getConfiguration().getGetSchoolDetailsUrl();
        var pathVariable = new ConcurrentHashMap<String, String>();
        pathVariable.put("schoolCode", schoolCode);
    return client
        .getRequestWithPathVariables(
            url, pathVariable, ClientHelper.getHeaders(), SchoolDetailsResponse.class)
        .map(
            response -> {
              log.info("School Details Response: {}", response);
              return response;
            })
        .onErrorMap(
                WebClientResponseException.NotFound.class, err -> {
              log.error(
                  "Exception occurred while trying to get school details for schoolId: {}",
                  schoolCode,
                  err);
              return new SchoolServiceUnavailableException("School not found: " + schoolCode, err);
            })
        .onErrorMap(
                WebClientResponseException.class, err -> {
              log.error(
                  "Exception occurred while trying to get school details for schoolId: {}",
                  schoolCode,
                    err);
                return new SchoolServiceUnavailableException("School service error: " +
                        err.getStatusCode(), err.getResponseBodyAsString());
            })
            .onErrorMap(Exception.class, err -> {
              log.error(
                  "Unexpected exception occurred while trying to get school details for schoolId: {}",
                  schoolCode,
                  err);
              return new SchoolServiceUnavailableException("Unexpected error occurred while fetching school details" +
                      err.getMessage(), err);
            });
    }

    @Override
    public Mono<Boolean> validateSchoolExists(String schoolCode) {

     return getSchoolDetails(schoolCode)
         .map(details -> true)
             .switchIfEmpty(Mono.just(false))
         .onErrorResume(
             SchoolServiceUnavailableException.class,
             err -> {
               log.warn("Service is currently not available {} does not exist.", schoolCode);
               return Mono.error(new SchoolServiceUnavailableException("School service is unavailable", err));
             })
             .onErrorMap(err -> {
               log.error("Error validating school existence for schoolCode: {}", schoolCode, err);
               return new SchoolNotFoundException("School not found: " + schoolCode + err.getMessage());
             });
    }
}
