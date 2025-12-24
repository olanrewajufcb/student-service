package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.DocumentType;

public record StudentDocumentRequest(

                                     DocumentType documentType,
                                     String fileName,
                                     String fileUrl,
                                     String mimeType,
                                     Long fileSize) {}
