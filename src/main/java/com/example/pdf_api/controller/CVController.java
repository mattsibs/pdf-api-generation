package com.example.pdf_api.controller;

import com.example.pdf_api.dto.CVRequest;
import com.example.pdf_api.dto.Skill;
import com.example.pdf_api.dto.Experience;
import com.example.pdf_api.dto.Education;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


@RestController
@RequestMapping("/cv")
public class CVController {
    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generateCVPdf(@RequestBody CVRequest cvRequest) {
        // PDF generation logic using PDFBox
        byte[] pdfBytes;
        try (PDDocument document = new PDDocument()) {
    // PDFBox 2.x font usage
PDType1Font helveticaBold = PDType1Font.HELVETICA_BOLD;
PDType1Font helvetica = PDType1Font.HELVETICA;
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);
            org.apache.pdfbox.pdmodel.PDPageContentStream contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page);

            float y = 700;
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 20);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Curriculum Vitae");
            contentStream.endText();
            y -= 40;

            // Personal Details
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 14);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Personal Details");
            contentStream.endText();
            y -= 20;
            contentStream.beginText();
            contentStream.setFont(helvetica, 12);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Name: " + cvRequest.getPersonalDetails().getName());
            contentStream.endText();
            y -= 15;
            contentStream.beginText();
            contentStream.setFont(helvetica, 12);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Email: " + cvRequest.getPersonalDetails().getEmail());
            contentStream.endText();
            y -= 15;
            contentStream.beginText();
            contentStream.setFont(helvetica, 12);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Phone: " + cvRequest.getPersonalDetails().getPhone());
            contentStream.endText();
            y -= 15;
            contentStream.beginText();
            contentStream.setFont(helvetica, 12);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Address: " + cvRequest.getPersonalDetails().getAddress());
            contentStream.endText();
            y -= 30;

            // Skills
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 14);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Skills");
            contentStream.endText();
            y -= 20;
            if (cvRequest.getSkills() != null) {
                for (com.example.pdf_api.dto.Skill skill : cvRequest.getSkills()) {
                    contentStream.beginText();
                    contentStream.setFont(helvetica, 12);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(skill.getName() + " (" + skill.getLevel() + ")");
                    contentStream.endText();
                    y -= 15;
                }
            }
            y -= 15;

            // Experience
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 14);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Experience");
            contentStream.endText();
            y -= 20;
            if (cvRequest.getExperience() != null) {
                for (com.example.pdf_api.dto.Experience exp : cvRequest.getExperience()) {
                    contentStream.beginText();
                    contentStream.setFont(helvetica, 12);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(exp.getPosition() + " at " + exp.getCompany() + " (" + exp.getStartDate() + " - " + exp.getEndDate() + ")");
                    contentStream.endText();
                    y -= 15;
                    contentStream.beginText();
                    contentStream.setFont(helvetica, 12);
                    contentStream.newLineAtOffset(70, y);
                    contentStream.showText(exp.getDescription());
                    contentStream.endText();
                    y -= 15;
                }
            }
            y -= 15;

            // Education
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 14);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Education");
            contentStream.endText();
            y -= 20;
            if (cvRequest.getEducation() != null) {
                for (com.example.pdf_api.dto.Education edu : cvRequest.getEducation()) {
                    contentStream.beginText();
                    contentStream.setFont(helvetica, 12);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(edu.getDegree() + " at " + edu.getInstitution() + " (" + edu.getStartDate() + " - " + edu.getEndDate() + ")");
                    contentStream.endText();
                    y -= 15;
                }
            }

            contentStream.close();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            document.save(baos);
            pdfBytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            pdfBytes = new byte[0];
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "cv.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
