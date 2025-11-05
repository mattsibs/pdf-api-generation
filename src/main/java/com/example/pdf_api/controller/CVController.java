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

import com.example.pdf_api.service.CVPdfService;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/cv")
public class CVController {
    @Autowired
    private CVPdfService cvPdfService;

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generateCVPdf(@RequestBody CVRequest cvRequest) {
        byte[] pdfBytes = cvPdfService.generatePdf(cvRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "cv.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
