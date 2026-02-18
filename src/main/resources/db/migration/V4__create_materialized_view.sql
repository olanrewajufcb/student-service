
CREATE MATERIALIZED VIEW student_schema.student_analytics_enrollment_summary AS
SELECT
    school_id,
    school_code,
    academic_year,
    COUNT(*) FILTER (WHERE enrollment_status = 'ACTIVE') AS total_students,
    COUNT(*) FILTER (WHERE enrollment_type = 'NEW_ADMISSION') AS new_admissions,
    COUNT(*) FILTER (WHERE enrollment_type = 'TRANSFER_IN') AS transfer_ins,
    COUNT(*) FILTER (WHERE enrollment_type = 'RE_ENROLLMENT') AS re_enrollments,
    COUNT(*) FILTER (WHERE enrollment_status = 'DROPPED_OUT') AS dropouts
FROM student_schema.student_enrollments
WHERE is_deleted = false
GROUP BY school_id, school_code, academic_year;


CREATE INDEX idx_student_analytics_school_year
    ON student_schema.student_analytics_enrollment_summary
        (school_code, academic_year);

CREATE MATERIALIZED VIEW student_schema.mv_student_dropout_risk AS
WITH base_calculation AS (
    SELECT
        s.student_id,
        s.student_number,
        s.school_id,
        s.school_code,
        s.academic_year,

        -- Attendance %
        ROUND(
                (COUNT(*) FILTER (WHERE a.attendance_status = 'PRESENT')::numeric
             / NULLIF(COUNT(a.attendance_id), 0)) * 100,
                2
        ) AS attendance_rate,

        COUNT(*) FILTER (WHERE a.attendance_status = 'ABSENT') AS absent_days,

        MAX(CASE WHEN s.enrollment_type = 'RE_ENROLLMENT' THEN 1 ELSE 0 END) AS is_reenrolled,
        MAX(CASE WHEN s.enrollment_type = 'TRANSFER_IN' THEN 1 ELSE 0 END) AS is_transfer,

        MAX(CASE WHEN s.dropout_date IS NOT NULL THEN 1 ELSE 0 END) AS has_dropout_history

    FROM student_schema.student_enrollments s
             LEFT JOIN student_schema.student_attendance_projection a
                       ON a.student_number = s.student_number
                           AND a.attendance_date >= CURRENT_DATE - INTERVAL '30 days'
    WHERE s.enrollment_status = 'ACTIVE'
    GROUP BY s.student_id, s.student_number, s.school_id, s.school_code, s.academic_year
)
SELECT
    student_id,
    student_number,
    school_id,
    school_code,
    academic_year,
    attendance_rate,
    absent_days,
    is_reenrolled,
    is_transfer,
    has_dropout_history,

    -- Risk score calculation using the pre-calculated attendance_rate
    (
        CASE WHEN attendance_rate < 60 THEN 40 ELSE 0 END
            +
        CASE WHEN absent_days > 10 THEN 25 ELSE 0 END  -- Using the pre-calculated absent_days
            +
        CASE WHEN has_dropout_history = 1 THEN 20 ELSE 0 END
            +
        CASE WHEN is_reenrolled = 1 THEN 15 ELSE 0 END
            +
        CASE WHEN is_transfer = 1 THEN 10 ELSE 0 END
        ) AS risk_score
FROM base_calculation;



CREATE MATERIALIZED VIEW student_schema.student_dropout_risk_level AS
SELECT
    student_id,
    school_id,
    academic_year,
    risk_score,
    CASE
        WHEN risk_score >= 60 THEN 'HIGH'
        WHEN risk_score >= 30 THEN 'MEDIUM'
        ELSE 'LOW'
        END AS risk_level
FROM student_schema.mv_student_dropout_risk
