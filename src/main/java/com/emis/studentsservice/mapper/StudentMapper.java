package com.emis.studentsservice.mapper;
import com.emis.studentsservice.domain.db.ContactInfo;
import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.ContactInfoRequest;
import com.emis.studentsservice.dto.request.CreateStudentRequest;
import com.emis.studentsservice.dto.response.StudentResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "schoolId", source = "request.schoolId")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "studentNumber", source = "studentNumber")
    @Mapping(target = "gradeLevel", source = "gradeLevel")
//    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Student toEntity(CreateStudentRequest request);

    ContactInfo toContactInfo(ContactInfoRequest request);

    @Mapping(target = "fullName", expression = "java(createFullName(student))")
    StudentResponse toResponse(Student student);

    default String createFullName(Student student) {
        String firstName = student.getFirstName() != null ? student.getFirstName() : "";
        String lastName = student.getLastName() != null ? student.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }

}
