package com.emis.studentsservice.service.report.impl;


import com.emis.studentsservice.exception.ReportGenerationException;
import com.emis.studentsservice.service.report.model.StudentListReportRow;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentListPdfGenerator {

    public byte[] generate(
            String schoolName,
            String academicYear,
            List<StudentListReportRow> students
    ) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            document.add(new Paragraph("STAFF LIST REPORT", titleFont));
            document.add(new Paragraph("School: " + schoolName));
            document.add(new Paragraph("Academic Year: " + academicYear));
            document.add(new Paragraph("Generated At: " + Instant.now()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
      addHeader(
          table,
          headerFont,
          "Student Number",
          "Student Id",
          "Full Name",
          "Academic Year",
          "School Code",
          "School Name",
          "Status");

            for (StudentListReportRow row : students) {
                table.addCell(new PdfPCell(new Phrase(row.getStudentNumber(), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(row.getFullName(), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(row.getStudentId()), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(row.getSchoolCode(), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(row.getSchoolName(), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(row.getAcademicYear(), bodyFont)));
                table.addCell(new PdfPCell(new Phrase(row.getStatus(), bodyFont)));
            }

            document.add(table);
            document.close();

            return baos.toByteArray();

        } catch (Exception ex) {
            throw new ReportGenerationException("Failed to generate staff list PDF", ex);
        }
    }

    private void addHeader(PdfPTable table, Font font, String... headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }
    }
}