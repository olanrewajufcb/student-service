DROP TABLE IF EXISTS schools;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS guardians;
DROP TABLE IF EXISTS medical_records;
DROP TABLE IF EXISTS student_documents;
DROP TABLE IF EXISTS enrolment_history;

-- CREATE TABLE schools (
--     school_id       BIGSERIAL PRIMARY KEY ,
--     school_code     VARCHAR(20) UNIQUE NOT NULL ,
--     school_name     VARCHAR(100)   NOT NULL ,
--     type            VARCHAR(50) NOT NULL ,
--     address         VARCHAR,
--     email           VARCHAR,
--     principal_name  VARCHAR,
--     max_students_per_class    INT,
--     school_capacity INT,
--     academic_calendar VARCHAR,
--     establishment_date TIMESTAMP,
--     status          VARCHAR(50),
--     city            VARCHAR,
--     lga             VARCHAR,
--     state           VARCHAR,
--     created_at      TIMESTAMP
-- )

CREATE TABLE students
(
    student_id      BIGSERIAL PRIMARY KEY,
    student_number  VARCHAR(50) UNIQUE NOT NULL,
    school_id       BIGINT NOT NULL, --FK to school service
    school_code     VARCHAR(20) NOT NULL default '', --denormalized to school service
    school_name     VARCHAR(50) NOT NULL default '', --denormalized to school service
    first_name      VARCHAR(100)       NOT NULL,
    last_name       VARCHAR(100)       NOT NULL,
    school_id       BIGINT NOT NULL,
    date_of_birth   DATE,
    gender          VARCHAR(20),
    enrollment_date TIMESTAMP,
    class_level     VARCHAR(20),
    status          VARCHAR(50) DEFAULT ACTIVE,
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
