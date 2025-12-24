package com.emis.studentsservice.domain.db;

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
@Table("guardians")
public class Guardian {
    @Id
    private Long guardianId;
    private Long studentId;
    private String firstName;
    private String lastName;
    private String relationship;
    private String email;
    private String phone;
    private String address;
    private Boolean isPrimaryContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
