package com.emis.studentsservice.mapper;


import com.emis.studentsservice.domain.db.Guardian;
import com.emis.studentsservice.dto.request.GuardianRequest;
import com.emis.studentsservice.dto.response.GuardianResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GuardianMapper {

    GuardianMapper INSTANCE = Mappers.getMapper(GuardianMapper.class);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Guardian toEntity(GuardianRequest request);

    GuardianResponse toResponse(Guardian guardian);

    List<GuardianResponse> toResponses(List<Guardian> guardian);
}
