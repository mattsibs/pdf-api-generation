package com.example.pdf_api.service;

import com.example.pdf_api.dto.CVRequest;
import com.example.pdf_api.dto.Skill;
import com.example.pdf_api.dto.Experience;
import com.example.pdf_api.dto.Education;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class CVPdfService {
    // Style constants
    private static final float LEFT_MARGIN = 50;
    private static final float INDENT = 20;
    private static final float SECTION_TITLE_SIZE = 14;
    private static final float SECTION_TITLE_SPACING = 20;
    private static final float ROW_FONT_SIZE = 12;
    private static final float ROW_SPACING = 15;
    private static final float SECTION_BOTTOM_SPACING = 15;
    private static final float TITLE_FONT_SIZE = 20;
    private static final float TITLE_BOTTOM_SPACING = 40;

    public byte[] generatePdf(CVRequest cvRequest) {
        byte[] pdfBytes;
        try (PDDocument document = new PDDocument()) {
            PDType1Font helveticaBold = PDType1Font.HELVETICA_BOLD;
            PDType1Font helvetica = PDType1Font.HELVETICA;
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float y = 700;
            y = writeTitle(contentStream, helveticaBold, y);
            y = writePersonalDetails(contentStream, helveticaBold, helvetica, y, cvRequest);
            y = writeSkills(contentStream, helveticaBold, helvetica, y, cvRequest);
            y = writeExperience(contentStream, helveticaBold, helvetica, y, cvRequest);
            y = writeEducation(contentStream, helveticaBold, helvetica, y, cvRequest);

            contentStream.close();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            document.save(baos);
            pdfBytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            pdfBytes = new byte[0];
        }
        return pdfBytes;
    }

    private float writeTitle(PDPageContentStream cs, PDType1Font font, float y) throws Exception {
        cs.beginText();
        cs.setFont(font, TITLE_FONT_SIZE);
        cs.newLineAtOffset(LEFT_MARGIN, y);
        cs.showText("Curriculum Vitae");
        cs.endText();
        return y - TITLE_BOTTOM_SPACING;
    }

    private float writeSection(PDPageContentStream cs, PDType1Font titleFont, PDType1Font rowFont, float y, String sectionTitle, java.util.List<String> rows, boolean indentRows) throws Exception {
        // Section title
        cs.beginText();
        cs.setFont(titleFont, SECTION_TITLE_SIZE);
        cs.newLineAtOffset(LEFT_MARGIN, y);
        cs.showText(sectionTitle);
        cs.endText();
        y -= SECTION_TITLE_SPACING;
        // Rows
        for (String row : rows) {
            cs.beginText();
            cs.setFont(rowFont, ROW_FONT_SIZE);
            cs.newLineAtOffset(indentRows ? LEFT_MARGIN + INDENT : LEFT_MARGIN, y);
            cs.showText(row);
            cs.endText();
            y -= ROW_SPACING;
        }
        return y - SECTION_BOTTOM_SPACING;
    }

    private float writePersonalDetails(PDPageContentStream cs, PDType1Font bold, PDType1Font regular, float y, CVRequest req) throws Exception {
        java.util.List<String> rows = new java.util.ArrayList<>();
        if (req.getPersonalDetails() != null) {
            rows.add("Name: " + req.getPersonalDetails().getName());
            rows.add("Email: " + req.getPersonalDetails().getEmail());
            rows.add("Phone: " + req.getPersonalDetails().getPhone());
            rows.add("Address: " + req.getPersonalDetails().getAddress());
        }
        return writeSection(cs, bold, regular, y, "Personal Details", rows, false);
    }

    private float writeSkills(PDPageContentStream cs, PDType1Font bold, PDType1Font regular, float y, CVRequest req) throws Exception {
        java.util.List<String> rows = new java.util.ArrayList<>();
        if (req.getSkills() != null) {
            for (Skill skill : req.getSkills()) {
                rows.add(skill.getName() + " (" + skill.getLevel() + ")");
            }
        }
        return writeSection(cs, bold, regular, y, "Skills", rows, false);
    }

    private float writeExperience(PDPageContentStream cs, PDType1Font bold, PDType1Font regular, float y, CVRequest req) throws Exception {
        java.util.List<String> rows = new java.util.ArrayList<>();
        if (req.getExperience() != null) {
            for (Experience exp : req.getExperience()) {
                rows.add(exp.getPosition() + " at " + exp.getCompany() + " (" + exp.getStartDate() + " - " + exp.getEndDate() + ")");
                if (exp.getDescription() != null && !exp.getDescription().isEmpty()) {
                    rows.add("  " + exp.getDescription()); // Indent description
                }
            }
        }
        return writeSection(cs, bold, regular, y, "Experience", rows, true);
    }

    private float writeEducation(PDPageContentStream cs, PDType1Font bold, PDType1Font regular, float y, CVRequest req) throws Exception {
        java.util.List<String> rows = new java.util.ArrayList<>();
        if (req.getEducation() != null) {
            for (Education edu : req.getEducation()) {
                rows.add(edu.getDegree() + " at " + edu.getInstitution() + " (" + edu.getStartDate() + " - " + edu.getEndDate() + ")");
            }
        }
        return writeSection(cs, bold, regular, y, "Education", rows, false);
    }
}
