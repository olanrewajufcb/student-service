package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.DocumentType;

public record DocumentUploadRequest(Long studentId,
                                    DocumentType documentType,
                                    String fileName,
                                    String fileUrl,
                                    String mimeType,
                                    Long fileSize) {}
