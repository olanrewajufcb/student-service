package com.emis.studentsservice.domain.db;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("medical_records")
public class MedicalRecord {
    @Id
    private Long recordId;
    private Long studentId;
    private String bloodType;
    private List<String> allergies;
    private List<String> chronicConditions;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
