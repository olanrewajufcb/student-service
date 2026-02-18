package com.emis.studentsservice.service.report;

import reactor.core.publisher.Mono;

public interface ReportFileStorage {
    Mono<String> upload(String fileName, byte[] content);
}