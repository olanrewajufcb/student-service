DROP TABLE IF EXISTS schools;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS guardians;
DROP TABLE IF EXISTS medical_records;
DROP TABLE IF EXISTS student_documents;
DROP TABLE IF EXISTS enrolment_history;


-- 0. Create ENUM types
CREATE TYPE orphan_status AS ENUM (
    'NONE', 'LOST_MOTHER', 'LOST_FATHER', 'LOST_BOTH'
);

CREATE TYPE disability_level AS ENUM (
    'NONE', 'MINOR', 'MODERATE', 'SEVERE'
);

CREATE TYPE disability_type AS ENUM (
    'NONE', 'VISUALLy_IMPAIRED', 'BLIND','HEARING_IMPAIRED', 'SPEECH_IMPAIRED', 'MENTALLY_CHALLENGED',
    'PHYSICALLY_CHALLENGED', 'AUTISM', 'OTHER'
);

CREATE TYPE status AS ENUM (
    'ACTIVE', 'INACTIVE', 'GRADUATED', 'DROPPED'
);

CREATE TYPE grade_level_type AS ENUM (
    'PRE_NURSERY', 'NURSERY_1', 'NURSERY_2',
    'PRIMARY_1', 'PRIMARY_2', 'PRIMARY_3',
    'PRIMARY_4', 'PRIMARY_5', 'PRIMARY_6',
    'JSS_1', 'JSS_2', 'JSS_3',
    'SSS_1', 'SSS_2', 'SSS_3'
);

CREATE TABLE students
(
    student_id      BIGSERIAL PRIMARY KEY,
    student_number  VARCHAR(50) UNIQUE NOT NULL,
    school_id       BIGINT NOT NULL, --FK to school service
    school_code     VARCHAR(20) NOT NULL default '', --denormalized to school service
    school_name     VARCHAR(50) NOT NULL default '', --denormalized to school service
    first_name      VARCHAR(100)       NOT NULL,
    last_name       VARCHAR(100)       NOT NULL,
    date_of_birth   DATE,
    gender          VARCHAR(20),
    enrollment_date TIMESTAMP,
    grade_level     VARCHAR(20),
    status          status DEFAULT ACTIVE,
    orphan_status orphan_status DEFAULT 'NONE',
    -- Embedded ContactInfo as columns:
    email           VARCHAR(255),
    phone           VARCHAR(50),
    address1   TEXT,
    address2  TEXT,
    city            VARCHAR(100),
    lga             VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(20),
    country         VARCHAR(100),
    photo_url       TEXT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
--     CONSTRAINT fk_student_school FOREIGN KEY (school_id)
--         REFERENCES schools(school_id) ON DELETE CASCADE
);

CREATE TABLE guardians (
                           guardian_id BIGSERIAL PRIMARY KEY,
                           student_id BIGINT NOT NULL,
                           first_name VARCHAR(100),
                           last_name VARCHAR(100),
                           relationship VARCHAR(50),
                           email VARCHAR(255),
                           phone VARCHAR(50),
                           address TEXT,
                           is_primary_contact BOOLEAN,
                           created_at TIMESTAMP DEFAULT NOW(),
                           updated_at TIMESTAMP DEFAULT NOW(),
                           CONSTRAINT fk_guardian_student FOREIGN KEY (student_id)
                               REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE TABLE medical_records (
    record_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL ,
    blood_type VARCHAR (100),
    allergies TEXT[],
    chronic_conditions TEXT[],
    notes TEXT,
    special_needs TEXT,
    disability_type disability_type DEFAULT 'NONE',
    disability_level disability_level DEFAULT 'NONE',
    special_education_needs BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_medical_student FOREIGN KEY (student_id)
        REFERENCES students(student_id) ON DELETE CASCADE


);
-- CREATE TABLE student_documents (
--                                    document_id BIGSERIAL PRIMARY KEY,
--                                    student_id  BIGINT NOT NULL,
--                                    document_type VARCHAR(50),
--                                    file_name   VARCHAR(255),
--                                    file_url    TEXT,
--                                    mime_type   VARCHAR(100),
--                                    file_size   BIGINT,
--                                    uploaded_date TIMESTAMP DEFAULT NOW(),
--                                    uploaded_by VARCHAR(255),
--
--                                    CONSTRAINT fk_document_student FOREIGN KEY (student_id)
--                                        REFERENCES students(student_id) ON DELETE CASCADE
-- );

CREATE TABLE enrolment_history (
                                   enrollment_id BIGSERIAL PRIMARY KEY,
                                   student_id BIGINT NOT NULL,
                                   school_year VARCHAR(20),
                                   grade_level VARCHAR(50),
                                   school_name VARCHAR(255),
                                   school_id BIGINT,
                                   type VARCHAR(50),
                                   note TEXT,
                                   effective_date TIMESTAMP,
                                   recorded_at TIMESTAMP DEFAULT NOW(),

                                   CONSTRAINT fk_enrolment_student FOREIGN KEY (student_id)
                                       REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE INDEX idx_students_student_number ON students(student_number);
CREATE INDEX idx_students_studentId ON students(student_id);
CREATE INDEX idx_students_school_id ON students(school_id);
CREATE INDEX idx_guardians_student_id ON guardians(student_id);
CREATE INDEX idx_medical_records_student_id ON medical_records(student_id);
CREATE INDEX idx_enrolment_history_student_id ON enrolment_history(student_id);
CREATE INDEX idx_special_needs_student ON special_needs(student_id);
CREATE INDEX idx_orphan_details_student ON orphan_details(student_id);
CREATE INDEX idx_students_orphan_status ON students(orphan_status);
CREATE INDEX idx_students_special_needs ON students(has_special_needs);