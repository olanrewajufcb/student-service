package com.emis.studentsservice.event.consumer;

import com.emis.studentsservice.event.DomainEvent;
import com.emis.studentsservice.event.outbox.ReportRequestedEvent;
import com.emis.studentsservice.repository.StudentReportRepository;
import com.emis.studentsservice.repository.StudentRepository;
import com.emis.studentsservice.service.report.ReportFileStorage;
import com.emis.studentsservice.service.report.impl.StudentListExcelGenerator;
import com.emis.studentsservice.service.report.impl.StudentListPdfGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReportProcessingService {

    private final StudentRepository studentRepository;
    private final StudentReportRepository reportRepository;
    private final StudentListPdfGenerator pdfGenerator;
    private final StudentListExcelGenerator excelGenerator;
    private final ReportFileStorage storage;
    private final ObjectMapper objectMapper;

    public Mono<Void> process(DomainEvent<JsonNode> event) {

        if (!"STUDENT_REPORT_REQUESTED".equals(event.getEventType())) {
            return Mono.empty();
        }

        ReportRequestedEvent payload =
                objectMapper.convertValue(event.getData(), ReportRequestedEvent.class);

        return reportRepository.findById(payload.getReportId())
                .flatMap(report ->
                        studentRepository.findBySchoolCode(payload.getSchoolCode())
                                .collectList()
                                .flatMap(students -> {

                                    byte[] file;
                                    String extension;

                                    if ("XLSX".equals(payload.getReportFormat())) {
                                        file = excelGenerator.generate(
                                                payload.getSchoolCode(),
                                                payload.getAcademicYear(),
                                                students);
                                        extension = "xlsx";
                                    } else {
                                        file = pdfGenerator.generate(
                                                payload.getSchoolCode(),
                                                payload.getAcademicYear(),
                                                students);
                                        extension = "pdf";
                                    }

                                    String filename =
                                            "student-report-%s-%s.%s"
                                                    .formatted(payload.getSchoolCode(),
                                                            payload.getAcademicYear(),
                                                            extension);

                                    return storage.upload(filename, file)
                                            .flatMap(url -> {
                                                report.setFilePath(url);
                                                report.setGenerationStatus("COMPLETED");
                                                report.setUpdatedAt(LocalDateTime.now());
                                                return reportRepository.save(report);
                                            });
                                })
                )
                .onErrorResume(ex -> {
                    log.error("Report failed", ex);
                    return reportRepository
                            .updateStatus(payload.getReportId(), "FAILED")
                            .then(Mono.error(ex));
                })
                .then();
    }
}