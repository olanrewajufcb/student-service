package com.emis.studentsservice.domain.db;

import com.emis.studentsservice.enums.DocumentType;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("student_documents")
public record StudentDocument(@Id Long documentId,
                              Long studentId,
                              DocumentType documentType,
                              String fileName,
                              String fileUrl,
                              String mimeType,
                              int fileSize,
                              LocalDateTime uploadedDate,
                              String uploadedBy) {}
