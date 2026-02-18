package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.dto.response.AnalyticsSummaryResponse;
import com.emis.studentsservice.dto.response.StudentDropoutRiskResponse;
import com.emis.studentsservice.repository.StudentEnrollmentRepository;
import com.emis.studentsservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;

    @Override
    public Mono<AnalyticsSummaryResponse> getEnrollmentSummary(String schoolCode,
                                       String academicYear, String requestId) {
        log.info("Getting enrollment summary for schoolCode: {}, academicYear: {} with {}",
                schoolCode, academicYear, requestId);
        return studentEnrollmentRepository.findEnrollmentSummaryBySchool(schoolCode, academicYear);
    }

    public Flux<AnalyticsSummaryResponse> getEnrollmentSummary(String academicYear, String requestId) {
        log.info("Getting enrollment summary with {}", requestId);
        return studentEnrollmentRepository.findEnrollmentSummaryByAcademicYear(academicYear);
    }

    @Override
    public Flux<StudentDropoutRiskResponse> getStudentDropoutRisk(String schoolCode, String requestId) {
        return studentEnrollmentRepository.findDropoutRiskBySchool(schoolCode);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshAnalyticsNightly() {
        studentEnrollmentRepository.refreshEnrollmentAnalytics().subscribe();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void refreshDropoutRisk() {
        studentEnrollmentRepository.refreshDropoutRiskView().subscribe();
    }
}
