package com.emis.studentsservice.mapper;
import com.emis.studentsservice.domain.db.EnrollmentHistory;
import com.emis.studentsservice.dto.request.EnrollmentHistoryRequest;
import com.emis.studentsservice.dto.response.EnrollmentResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "enrollmentId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "note", source = "notes")  // Map 'notes' from request to 'note' in entity
    EnrollmentHistory toEntity(EnrollmentHistoryRequest request);

    @Mapping(target = "note", source = "note")
    EnrollmentResponse toResponse(EnrollmentHistory history);

  }
