package com.emis.studentsservice.service.client;

import com.emis.studentsservice.dto.response.SchoolDetailsResponse;
import reactor.core.publisher.Mono;

public interface SchoolService {

    Mono<SchoolDetailsResponse> getSchoolDetails(String schoolCode);

    Mono<Boolean> validateSchoolExists(String schoolId);
}
