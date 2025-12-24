package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.domain.db.StudentDocument;
import com.emis.studentsservice.dto.request.DocumentUploadRequest;
import com.emis.studentsservice.helper.StudentServiceHelper;
import com.emis.studentsservice.repository.StudentDocumentRepository;
import com.emis.studentsservice.service.DocumentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final StudentDocumentRepository documentRepository;
    private final StudentServiceHelper serviceHelper;

    public Mono<StudentDocument> uploadDocument(Long studentId, DocumentUploadRequest request) {
        log.info("Uploading document for student: {}, type: {}", studentId, request.documentType());

        return serviceHelper.validateStudentExists(studentId)
                .then(buildStudentDocument(studentId, request))
                .flatMap(documentRepository::save)
                .doOnSuccess(document -> log.info("Document uploaded successfully: {}", document.documentId()))
                .doOnError(error -> log.error("Failed to upload document: {}", error.getMessage()));
    }

    private Mono<StudentDocument> buildStudentDocument(Long studentId, DocumentUploadRequest request) {
        return Mono.just(new StudentDocument(null,studentId, request.documentType(), request.fileName(),
                "","",0, LocalDateTime.now(), null));
    }
}
