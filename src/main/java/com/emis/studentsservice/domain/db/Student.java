package com.emis.studentsservice.domain.db;


import com.emis.studentsservice.enums.Gender;
import com.emis.studentsservice.enums.GradeLevel;
import com.emis.studentsservice.enums.OrphanStatus;
import com.emis.studentsservice.enums.StudentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "students")
public class Student {
    @Id
    private Long studentId;
    private Long schoolId;
    private String schoolCode;
    private String schoolName;
    private String studentNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private LocalDateTime enrollmentDate;
    private String gradeLevel;
    private String status;
    private String orphanStatus;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String lga;
    private String state;
    private String postalCode;
    private Boolean isDeleted;
    private LocalDate deletedAt;
    private String photoUrl;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
