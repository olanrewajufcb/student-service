package com.emis.studentsservice.mapper;

import com.emis.studentsservice.domain.db.MedicalRecord;
import com.emis.studentsservice.dto.request.MedicalRecordRequest;
import com.emis.studentsservice.dto.response.MedicalRecordResponse;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MedicalMapper {

    MedicalMapper INSTANCE = Mappers.getMapper(MedicalMapper.class);

    @Mapping(target = "recordId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "allergies", source = "allergies", qualifiedByName = "stringToList")
    @Mapping(target = "chronicConditions", source = "chronicConditions", qualifiedByName = "stringToList")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    MedicalRecord toEntity(MedicalRecordRequest request);

    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);

    @Named("stringToList")
    default List<String> stringToList(String value) {
        if (value == null || value.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }


}
