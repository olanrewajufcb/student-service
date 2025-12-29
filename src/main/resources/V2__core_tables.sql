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
    grade_level     grade_level_type,
    status          student_status DEFAULT 'ACTIVE',
    orphan_status   orphan_status DEFAULT 'NONE',
    email           VARCHAR(255),
    phone           VARCHAR(50),
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
                                          student_id BIGINT NOT NULL,
                                          first_name VARCHAR(100),
                                          last_name VARCHAR(100),
                                          relationship VARCHAR(50),
                                          email VARCHAR(255),
                                          phone VARCHAR(50),
                                          address TEXT,
                                          is_primary_contact BOOLEAN,
                                          is_deleted BOOLEAN DEFAULT FALSE,
                                          deleted_at TIMESTAMPZ  DEFAULT NULL,
                                          created_at TIMESTAMPZ DEFAULT NOW(),
                                          updated_at TIMESTAMPZ DEFAULT NOW(),
                                          CONSTRAINT fk_guardian_student FOREIGN KEY (student_id)
                                              REFERENCES student_schema.students(student_id) ON DELETE RESTRICT
);

CREATE TABLE student_schema.medical_records (
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
                                                is_deleted BOOLEAN DEFAULT FALSE,
                                                deleted_at TIMESTAMPZ  DEFAULT NULL,
                                                created_at TIMESTAMPZ DEFAULT NOW(),
                                                updated_at TIMESTAMPZ DEFAULT NOW(),

                                                CONSTRAINT fk_medical_student FOREIGN KEY (student_id)
                                                    REFERENCES student_schema.students(student_id) ON DELETE RESTRICT


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
                                                  grade_level grade_level_type,
                                                  school_name VARCHAR(255),
                                                  school_id BIGINT,
                                                  type VARCHAR(50),
                                                  note TEXT,
                                                  effective_date TIMESTAMPZ,
                                                  is_deleted BOOLEAN DEFAULT FALSE,
                                                  deleted_at TIMESTAMPZ  DEFAULT NULL,
                                                  recorded_at TIMESTAMPZ DEFAULT NOW(),

                                                  CONSTRAINT fk_enrolment_student FOREIGN KEY (student_id)
                                                      REFERENCES student_schema.students(student_id) ON DELETE RESTRICT
);