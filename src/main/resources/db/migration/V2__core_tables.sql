CREATE TABLE student_schema.students
(
    student_id      BIGSERIAL PRIMARY KEY,
    student_number  VARCHAR(50) UNIQUE NOT NULL,
    school_id       BIGINT NOT NULL,
    school_code     VARCHAR(20) NOT NULL DEFAULT '',
    school_name     VARCHAR(50) NOT NULL DEFAULT '',
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    date_of_birth   DATE,
    gender          VARCHAR(20),
    enrollment_date TIMESTAMP,
    grade_level     VARCHAR(20),
    status          VARCHAR(20) DEFAULT 'ACTIVE',
    orphan_status   VARCHAR(20) DEFAULT 'NONE',
    email           VARCHAR(50),
    phone           VARCHAR(30),
    address1        TEXT,
    address2        TEXT,
    city            VARCHAR(100),
    lga             VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(20),
    country         VARCHAR(100),
    is_deleted      BOOLEAN DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ DEFAULT NULL,
    photo_url       TEXT,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);


CREATE TABLE student_schema.guardians (
                                          guardian_id BIGSERIAL PRIMARY KEY,
                                          student_id BIGINT NOT NULL REFERENCES student_schema.students(student_id) ON DELETE RESTRICT,
                                          first_name VARCHAR(100),
                                          last_name VARCHAR(100),
                                          relationship VARCHAR(50),
                                          email VARCHAR(255),
                                          phone VARCHAR(50),
                                          address TEXT,
                                          is_primary_contact BOOLEAN,
                                          is_deleted BOOLEAN DEFAULT FALSE,
                                          deleted_at TIMESTAMPTZ  DEFAULT NULL,
                                          created_at TIMESTAMPTZ DEFAULT NOW(),
                                          updated_at TIMESTAMPTZ DEFAULT NOW()

);

CREATE TABLE student_schema.medical_records (
                                                record_id BIGSERIAL PRIMARY KEY,
                                                student_id BIGINT NOT NULL REFERENCES student_schema.students(student_id) ON DELETE RESTRICT,
                                                blood_type VARCHAR (100),
                                                allergies TEXT[],
                                                chronic_conditions TEXT[],
                                                notes TEXT,
                                                special_needs TEXT,
                                                disability_type VARCHAR(20) DEFAULT 'NONE',
                                                disability_level VARCHAR(20) DEFAULT 'NONE',
                                                special_education_needs BOOLEAN DEFAULT FALSE,
                                                is_deleted BOOLEAN DEFAULT FALSE,
                                                deleted_at TIMESTAMPTZ  DEFAULT NULL,
                                                created_at TIMESTAMPTZ DEFAULT NOW(),
                                                updated_at TIMESTAMPTZ DEFAULT NOW()



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

CREATE TABLE student_schema.enrolment_history (
                                                  enrollment_id BIGSERIAL PRIMARY KEY,
                                                  student_id BIGINT NOT NULL,
                                                  school_year VARCHAR(20),
                                                  grade_level VARCHAR(20),
                                                  school_name VARCHAR(255),
                                                  school_id BIGINT,
                                                  type VARCHAR(50),
                                                  note TEXT,
                                                  effective_date TIMESTAMPTZ,
                                                  is_deleted BOOLEAN DEFAULT FALSE,
                                                  deleted_at TIMESTAMPTZ  DEFAULT NULL,
                                                  recorded_at TIMESTAMPTZ DEFAULT NOW(),

                                                  CONSTRAINT fk_enrolment_student FOREIGN KEY (student_id)
                                                      REFERENCES student_schema.students(student_id) ON DELETE RESTRICT
);

CREATE TABLE student_schema.student_enrollments (
                                                    enrollment_id        BIGSERIAL PRIMARY KEY,

                                                    student_id           BIGINT NOT NULL,
                                                    student_number       VARCHAR(50) NOT NULL,

                                                    school_id            BIGINT NOT NULL,
                                                    school_code          VARCHAR(20) NOT NULL,

--                                                     class_id             BIGINT NOT NULL,
--                                                     class_name           VARCHAR(20) NOT NULL,

                                                    academic_year        VARCHAR(9) NOT NULL, -- e.g. 2024/2025

                                                    enrollment_type      VARCHAR(20) NOT NULL,
    -- NEW | TRANSFER_IN | REPEAT

                                                    enrollment_status    VARCHAR(20) NOT NULL default 'ACTIVE',
    -- ACTIVE | TRANSFERRED_OUT | DROPPED_OUT | COMPLETED

                                                    enrollment_date      DATE NOT NULL DEFAULT NOW(),
                                                    completion_date            DATE,
                                                    exit_date             DATE,
                                                    exit_reason         VARCHAR(20),

                                                    remarks          TEXT,
    -- TRANSFER_OUT | DROPOUT | COMPLETION

                                                    dropout_reason      VARCHAR(100),
                                                    dropout_date        DATE,
                                                    progression_status  VARCHAR(20), --PROMOTED | REPEATED | REPEAT_FAILED,NONE
                                                    promoted_to_class    VARCHAR(20),

                                                    created_at           TIMESTAMP NOT NULL DEFAULT now(),
                                                    updated_at           TIMESTAMP,
                                                    is_deleted           BOOLEAN DEFAULT FALSE


);

CREATE UNIQUE INDEX uq_student_active_enrollment
    ON student_schema.student_enrollments(student_id, academic_year)
    WHERE enrollment_status = 'ACTIVE';


CREATE TABLE student_schema.student_reports (
                                                report_id BIGSERIAL PRIMARY KEY,
                                                school_id BIGINT,
                                                school_code VARCHAR(50),
                                                academic_year VARCHAR(10),

                                                report_type VARCHAR(50) NOT NULL, -- STUDENT_LIST, ENROLLMENT_SUMMARY, DROPOUT_REPORT, etc
                                                report_format VARCHAR(10) NOT NULL, -- PDF, XLSX

                                                generation_status VARCHAR(20) NOT NULL, -- GENERATING, COMPLETED, FAILED
                                                file_path TEXT,

                                                requested_by VARCHAR(100),
                                                created_at TIMESTAMP DEFAULT NOW(),
                                                updated_at TIMESTAMP
);

CREATE TABLE student_schema.outbox_events(

                                         outbox_id BIGSERIAL PRIMARY KEY,
                                         event_id UUID NOT NULL,
                                         aggregate_type VARCHAR(50) NOT NULL,
                                         aggregate_id VARCHAR(100) NOT NULL,
                                         event_type VARCHAR(100) NOT NULL,
                                         topic VARCHAR(200) NOT NULL,
                                         payload JSONB NOT NULL,
                                         status VARCHAR(20) DEFAULT 'PENDING',
                                         retry_count INT DEFAULT 0,
                                         created_at TIMESTAMPTZ DEFAULT NOW(),
                                         published_at TIMESTAMPTZ

);

CREATE TABLE student_schema.consumed_events (
                                                 event_id UUID PRIMARY KEY,
                                                 event_type VARCHAR(100),
                                                 consumed_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE student_schema.student_dropout_risk (
                                                     risk_id BIGSERIAL PRIMARY KEY,
                                                     student_id BIGINT NOT NULL,
                                                     school_id BIGINT NOT NULL,
                                                     academic_year VARCHAR(20) NOT NULL,

                                                     attendance_score INTEGER DEFAULT 0,
                                                     academic_score INTEGER DEFAULT 0,
                                                     behavior_score INTEGER DEFAULT 0,

                                                     risk_score INTEGER NOT NULL,

                                                     last_calculated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     is_deleted BOOLEAN DEFAULT FALSE,

                                                     UNIQUE(student_id, academic_year)
);

CREATE TABLE student_schema.student_attendance_projection (

                                                              attendance_id BIGINT PRIMARY KEY,
                                                              student_number VARCHAR(50) NOT NULL,
                                                              section_id BIGINT NOT NULL,
                                                              school_code VARCHAR(50) NOT NULL,
                                                              notes TEXT,
                                                              correlation_id VARCHAR(50),
                                                              attendance_date DATE NOT NULL,
                                                              attendance_status VARCHAR(20) NOT NULL,
                                                              recorded_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_projection_student_date
    ON student_schema.student_attendance_projection(student_number, attendance_date);