package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.StudentReport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StudentReportRepository extends ReactiveCrudRepository<StudentReport, Long> {

  @Query(
      """
       UPDATE student_schema.student_reports
       SET generation_status = $2
       WHERE report_id = $1
       """)
  Mono<Void> updateStatus(Long reportId, String status);
}
