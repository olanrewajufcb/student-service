package com.emis.studentsservice.service.report.impl;

import com.emis.studentsservice.domain.StudentReport;
import com.emis.studentsservice.domain.db.OutboxEvent;
import com.emis.studentsservice.dto.request.GenerateStudentReportRequest;
import com.emis.studentsservice.dto.response.GenerateStudentReportResponse;
import com.emis.studentsservice.dto.response.StudentReportDetailsResponse;
import com.emis.studentsservice.event.publisher.OutboxEventRepository;
import com.emis.studentsservice.event.outbox.ReportRequestedEvent;
import com.emis.studentsservice.exception.ResourceNotFoundException;
import com.emis.studentsservice.repository.StudentReportRepository;
import com.emis.studentsservice.service.report.StudentReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentReportServiceImpl implements StudentReportService {
    private final StudentReportRepository reportRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<GenerateStudentReportResponse> generateReport(
            GenerateStudentReportRequest request,
            String requestId
    ) {

        StudentReport report = StudentReport.builder()
                .schoolCode(request.schoolCode())
                .academicYear(request.academicYear())
                .reportType(request.reportType().name())
                .reportFormat(request.format().name())
                .generationStatus("GENERATING")
                .createdAt(LocalDateTime.now())
                .build();

        return reportRepository.save(report)
                .flatMap(saved -> {

                    ReportRequestedEvent event =
                            ReportRequestedEvent.builder()
                                    .reportId(saved.getReportId())
                                    .reportType(saved.getReportType())
                                    .reportFormat(saved.getReportFormat())
                                    .schoolCode(saved.getSchoolCode())
                                    .academicYear(saved.getAcademicYear())
                                    .build();

                    return outboxRepository.save(
                            OutboxEvent.builder()
                                    .eventId(UUID.randomUUID())
                                    .aggregateType("STUDENT_REPORT")
                                    .aggregateId(saved.getReportId().toString())
                                    .eventType("STUDENT_REPORT_REQUESTED")
                                    .topic("student.report.events.v1")
                                    .payload(objectMapper.valueToTree(event))
                                    .status("PENDING")
                                    .build()
                    ).thenReturn(saved);
                })
                .map(saved ->
                        new GenerateStudentReportResponse(
                                saved.getReportId(),
                                saved.getGenerationStatus(),
                                saved.getCreatedAt()
                        ))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<StudentReportDetailsResponse> getReport(Long reportId) {
        return reportRepository.findById(reportId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Report not found")))
                .map(StudentReportDetailsResponse::from);
    }

}
