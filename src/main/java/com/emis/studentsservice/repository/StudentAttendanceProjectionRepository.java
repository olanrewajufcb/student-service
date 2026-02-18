package com.emis.studentsservice.repository;

import com.emis.studentsservice.event.AttendanceEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StudentAttendanceProjectionRepository extends ReactiveCrudRepository<AttendanceEvent, Long> {}
