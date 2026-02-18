package com.emis.studentsservice.event.outbox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReportRequestedEvent {
    private Long reportId;
    private String reportType;
    private String reportFormat;
    private String schoolCode;
    private String academicYear;

}
