package com.email_and_pdf.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email_and_pdf.model.FormRequest;
import com.email_and_pdf.services.PdfService;

@RestController
@RequestMapping("/api/form")
@CrossOrigin(origins = "http://localhost:5173")
public class FormController {

    private final PdfService pdfService;

    public FormController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/submit")
    public ResponseEntity<byte[]> generatePdfAndSendEmail(@RequestBody FormRequest formRequest) {
        byte[] pdfBytes = pdfService.generatePdf(formRequest);

        //sending via email
        pdfService.sendEmailWithAttachment(formRequest.getEmail(), pdfBytes);
        
        //directly downloading Pdf 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "application_form.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
