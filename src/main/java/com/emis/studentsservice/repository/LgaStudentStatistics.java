package com.emis.studentsservice.repository;

public interface LgaStudentStatistics {
    String getLga();
    Long getTotalSchools();
    Long getTotalStudents();
    Long getActiveStudents();
}