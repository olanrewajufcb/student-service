package com.emis.studentsservice.service.report.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import com.emis.studentsservice.exception.ReportGenerationException;
import com.emis.studentsservice.service.report.model.StudentListReportRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentListExcelGenerator {

    public byte[] generate(
            String schoolName,
            String academicYear,
            List<StudentListReportRow> students
    ) {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Student List");

            int rowIdx = 0;

            // ===== Header Info =====
            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.createCell(0).setCellValue("STUDENT LIST REPORT");

            Row meta1 = sheet.createRow(rowIdx++);
            meta1.createCell(0).setCellValue("School:");
            meta1.createCell(1).setCellValue(schoolName);

            Row meta2 = sheet.createRow(rowIdx++);
            meta2.createCell(0).setCellValue("Academic Year:");
            meta2.createCell(1).setCellValue(academicYear);

            Row meta3 = sheet.createRow(rowIdx++);
            meta3.createCell(0).setCellValue("Generated At:");
            meta3.createCell(1).setCellValue(LocalDateTime.now().toString());

            rowIdx++; // blank row

            // ===== Table Header =====
            Row header = sheet.createRow(rowIdx++);
            createHeader(header,
                    "Student Number",
                    "Student Id",
                    "Full Name",
                    "Academic Year",
                    "School Code",
                    "School Name",
                    "Status"
            );

            // ===== Data Rows =====
            for (StudentListReportRow row : students) {
                Row data = sheet.createRow(rowIdx++);
                data.createCell(0).setCellValue(row.getStudentNumber());
                data.createCell(1).setCellValue(row.getFullName());
                data.createCell(2).setCellValue(row.getStudentId());
                data.createCell(4).setCellValue(row.getSchoolCode());
                data.createCell(5).setCellValue(row.getSchoolName());
                data.createCell(6).setCellValue(row.getStatus());
                data.createCell(7).setCellValue(row.getAcademicYear());
            }

            // Autosize columns
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            workbook.dispose();

            return out.toByteArray();

        } catch (Exception ex) {
            throw new ReportGenerationException("Failed to generate staff list Excel", ex);
        }
    }

    private void createHeader(Row row, String... headers) {
        CellStyle style = row.getSheet().getWorkbook().createCellStyle();
        Font font = row.getSheet().getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }
}