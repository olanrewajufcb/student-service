package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.StudentDocument;
import com.emis.studentsservice.enums.DocumentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentDocumentRepository extends R2dbcRepository<StudentDocument, Long> {
    
    @Query("SELECT * FROM student_documents WHERE student_id = :studentId ORDER BY upload_date DESC")
    Flux<StudentDocument> findByStudentId(Long studentId);
    
    @Query("SELECT * FROM student_documents WHERE student_id = :studentId AND document_type = :documentType")
    Flux<StudentDocument> findByStudentIdAndDocumentType(Long studentId, DocumentType documentType);
    
    @Query("SELECT * FROM student_documents WHERE document_type = :documentType")
    Flux<StudentDocument> findByDocumentType(DocumentType documentType);
    
    @Query("SELECT COUNT(*) FROM student_documents WHERE student_id = :studentId")
    Mono<Long> countByStudentId(Long studentId);
    
    @Query("DELETE FROM student_documents WHERE student_id = :studentId")
    Mono<Void> deleteByStudentId(Long studentId);
    
    @Query("SELECT * FROM student_documents WHERE file_size > :minSize ORDER BY file_size DESC")
    Flux<StudentDocument> findLargeDocuments(long minSize, Pageable pageable);
}