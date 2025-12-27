package com.emis.studentsservice.dto.response;

import java.util.Map;

public record StudentStatisticsResponse(Long totalStudents, Long activeStudents,
                                        Map<String, Long> byStatus,
                                        Map<String, Long> byGradeLevel,
                                        Map<String, Long> byGender) {}
