package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.dto.ContactInfoRequest;
import com.emis.studentsservice.enums.Gender;
import com.emis.studentsservice.enums.GradeLevel;
import com.emis.studentsservice.enums.OrphanStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateStudentRequest(
        @NotNull
        Long schoolId,
        @NotNull(message = "schoolCode is required")
        String schoolCode,
        @NotNull(message = "studentNumber is required")
        @Pattern(
                regexp = "^[A-Z]{2}\\d+$",
                message = "The studentNumber must start with 2 letters followed by digits, e.g., TT1273."
        )
        String studentNumber,
        @NotNull(message = "firstName is required")
        @NotBlank(message = "firstName cannot be blank")
        @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "firstName contains invalid characters")
        String firstName,
        @NotNull(message = "lastName is required")
        @NotBlank(message = "lastName cannot be blank")
        @Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "lastName contains invalid characters")
        String lastName,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotNull(message = "Gender is required")
        Gender gender,
        String email,
        String address1,
        String address2,
        String city,
        String ward,
        String lga,
        String postalCode,
        String state,
        @NotNull(message = "enrollment date is required")
        @PastOrPresent(message = "Enrollment date cannot be in the future")
        LocalDateTime enrollmentDate,
        @NotBlank(message = "Grade level is required")
        @Pattern(regexp = "^(K|[1-9]|1[0-2])$", message = "Grade level must be K or 1-12")
        GradeLevel gradeLevel,
        OrphanStatus orphanStatus,
        @Valid
        ContactInfoRequest contactInfo,
        @NotNull(message = "At least one guardian is required")
        @Size(min = 1, message = "At least one guardian is required")
        @Valid
        List<GuardianRequest> guardians,
        @Valid
        MedicalRecordRequest medicalInfo,



        String photoUrl
) {  public CreateStudentRequest {
    // Ensure enrollment date is after birth date
    if (dateOfBirth != null
        && enrollmentDate != null
        && enrollmentDate.toLocalDate().isBefore(dateOfBirth.plusYears(3))) {
            throw new IllegalArgumentException(
                    "Enrollment date must be at least 3 years after date of birth"
            );
        }

    if (guardians != null) {
        long primaryCount = guardians.stream()
                .filter(g -> Boolean.TRUE.equals(g.isPrimaryContact()))
                .count();
        if (primaryCount != 1) {
            throw new IllegalArgumentException(
                    "Exactly one guardian must be marked as primary contact"
            );
        }
    }
}}
