package com.example.pdf_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CVControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGenerateCVPdfAndVerifyContents() throws Exception {
        // Build sample CVRequest
        var cvRequest = new java.util.HashMap<String, Object>();
        var personalDetails = new java.util.HashMap<String, String>();
        personalDetails.put("name", "John Doe");
        personalDetails.put("email", "john.doe@example.com");
        personalDetails.put("phone", "1234567890");
        personalDetails.put("address", "123 Main St, City");
        cvRequest.put("personalDetails", personalDetails);

        var skills = List.of(
                java.util.Map.of("name", "Java", "level", "Expert"),
                java.util.Map.of("name", "Spring Boot", "level", "Advanced")
        );
        cvRequest.put("skills", skills);

        var experience = List.of(
                java.util.Map.of(
                        "company", "Acme Corp",
                        "position", "Software Engineer",
                        "startDate", "2020",
                        "endDate", "2022",
                        "description", "Developed REST APIs"
                )
        );
        cvRequest.put("experience", experience);

        var education = List.of(
                java.util.Map.of(
                        "institution", "State University",
                        "degree", "BSc Computer Science",
                        "startDate", "2016",
                        "endDate", "2020"
                )
        );
        cvRequest.put("education", education);

        String json = objectMapper.writeValueAsString(cvRequest);

        // Call the endpoint
        MvcResult result = mockMvc.perform(post("/cv/pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        byte[] pdfBytes = result.getResponse().getContentAsByteArray();

        // Save to temp file in target/
        File targetDir = new File("target");
        if (!targetDir.exists()) targetDir.mkdirs();
        File pdfFile = new File(targetDir, "cv-test.pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            fos.write(pdfBytes);
        }

                // Use Apache Tika to verify contents
        org.apache.tika.Tika tika = new org.apache.tika.Tika();
        String text = tika.parseToString(pdfFile);
        // Check for expected content
        assertThat(text).contains("John Doe");
        assertThat(text).contains("john.doe@example.com");
        assertThat(text).contains("Java (Expert)");
        assertThat(text).contains("Software Engineer at Acme Corp (2020 - 2022)");
        assertThat(text).contains("BSc Computer Science at State University (2016 - 2020)");
    }
}
