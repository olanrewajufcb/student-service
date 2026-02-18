-- =============================================
-- Student Service Schema (Nigeria Context) - Soft Delete Version
-- Microservices: No FKs to external services
-- =============================================

-- =============================================
-- Student Service Schema - V1
-- =============================================

CREATE SCHEMA IF NOT EXISTS student_schema AUTHORIZATION student_user;

GRANT USAGE, CREATE ON SCHEMA student_schema TO student_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA student_schema
GRANT ALL ON TABLES TO student_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA student_schema
GRANT ALL ON SEQUENCES TO student_user;

-- =====================
-- ENUM Types
-- =====================

-- CREATE TYPE student_schema.orphan_status AS ENUM (
--   'NONE', 'LOST_MOTHER', 'LOST_FATHER', 'LOST_BOTH'
-- );
--
-- CREATE TYPE student_schema.disability_level AS ENUM (
--   'NONE', 'MINOR', 'MODERATE', 'SEVERE'
-- );
--
-- CREATE TYPE student_schema.disability_type AS ENUM (
--   'NONE',
--   'VISUALLY_IMPAIRED',
--   'BLIND',
--   'HEARING_IMPAIRED',
--   'SPEECH_IMPAIRED',
--   'MENTALLY_CHALLENGED',
--   'PHYSICALLY_CHALLENGED',
--   'AUTISM',
--   'OTHER'
-- );
--
-- CREATE TYPE student_schema.student_status AS ENUM (
--   'ACTIVE', 'INACTIVE', 'GRADUATED', 'DROPPED'
-- );
--
-- CREATE TYPE student_schema.grade_level_type AS ENUM (
--   'PRE_NURSERY',
--   'NURSERY_1', 'NURSERY_2',
--   'PRIMARY_1', 'PRIMARY_2', 'PRIMARY_3',
--   'PRIMARY_4', 'PRIMARY_5', 'PRIMARY_6',
--   'JSS_1', 'JSS_2', 'JSS_3',
--   'SSS_1', 'SSS_2', 'SSS_3'
-- );
