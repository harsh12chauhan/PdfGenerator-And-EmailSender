package com.email_and_pdf.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.email_and_pdf.model.FormRequest;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import jakarta.mail.internet.MimeMessage;


@Service
public class PdfService {

    private final JavaMailSender mailSender;

    public PdfService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Generate PDF
    public byte[] generatePdf(FormRequest formRequest) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Application Form"));
            document.add(new Paragraph("Name: " + formRequest.getName()));
            document.add(new Paragraph("Email: " + formRequest.getEmail()));
            document.add(new Paragraph("Message: " + formRequest.getMessage()));

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    
//    public byte[] generatePdf(FormRequest formRequest) {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            // Initialize PdfWriter and PdfDocument
//            PdfWriter writer = new PdfWriter(outputStream);
//            PdfDocument pdf = new PdfDocument(writer);
//            Document document = new Document(pdf);
//
//            // Set a custom font for the document (optional)
//            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//
//            // Add a title with larger font size and centered alignment
//            document.add(new Paragraph("Application Form")
//                    .setFont(font)
//                    .setFontSize(18)
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setBold());
//
//            // Add a line separator
//            document.add(new LineSeparator());
//
//            // Add Name with styling
//            document.add(new Paragraph("Name: " + formRequest.getName())
//                    .setFontSize(12)
//                    .setBold()
//                    .setMarginTop(10));
//
//            // Add Email with styling
//            document.add(new Paragraph("Email: " + formRequest.getEmail())
//                    .setFontSize(12)
//                    .setItalic()
//                    .setMarginTop(5));
//
//            // Add Message with styling
//            document.add(new Paragraph("Message: " + formRequest.getMessage())
//                    .setFontSize(12)
//                    .setMarginTop(5));
//
//            // Add a table for a better layout (optional)
//            Table table = new Table(2); // Two columns
//            table.addCell(new Cell().add(new Paragraph("Field").setBold()));
//            table.addCell(new Cell().add(new Paragraph("Details")));
//
//            table.addCell(new Cell().add(new Paragraph("Name")));
//            table.addCell(new Cell().add(new Paragraph(formRequest.getName())));
//
//            table.addCell(new Cell().add(new Paragraph("Email")));
//            table.addCell(new Cell().add(new Paragraph(formRequest.getEmail())));
//
//            table.addCell(new Cell().add(new Paragraph("Message")));
//            table.addCell(new Cell().add(new Paragraph(formRequest.getMessage())));
//
//            document.add(table);
//
//            // Close the document
//            document.close();
//
//            return outputStream.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate PDF", e);
//        }
//    }


    // Send PDF via Email
    public void sendEmailWithAttachment(String recipientEmail, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject("Your Application Form");
            helper.setText("Please find your application form attached.");

            helper.addAttachment("application_form.pdf", new ByteArrayResource(pdfBytes));
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

