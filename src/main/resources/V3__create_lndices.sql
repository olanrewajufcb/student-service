
CREATE INDEX idx_students_student_number ON student_schema.students(student_number);
CREATE INDEX idx_students_studentId ON student_schema.students(student_id);
CREATE INDEX idx_students_school_id ON student_schema.students(school_id);
CREATE INDEX idx_guardians_student_id ON student_schema.guardians(student_id);
CREATE INDEX idx_medical_records_student_id ON student_schema.medical_records(student_id);
CREATE INDEX idx_enrolment_history_student_id ON student_schema.enrolment_history(student_id);
CREATE INDEX idx_students_orphan_status ON student_schema.students(orphan_status);
CREATE INDEX idx_students_student_number_not_deleted
    ON student_schema.students(student_number)
    WHERE is_deleted = FALSE;



CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_students_updated_at
    BEFORE UPDATE ON student_schema.students
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger for guardians table
CREATE TRIGGER trg_guardians_updated_at
    BEFORE UPDATE ON student_schema.guardians
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger for medical_records table
CREATE TRIGGER trg_medical_records_updated_at
    BEFORE UPDATE ON student_schema.medical_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger for enrolment_history table
CREATE TRIGGER trg_enrolment_history_updated_at
    BEFORE UPDATE ON student_schema.enrolment_history
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();


