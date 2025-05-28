package br.com.Qolli.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ProfileExportService {

    public byte[] generateProfilePdf(String userId) {
        DocumentSnapshot snapshot = getUserDocument(userId);

        String name = snapshot.getString("name");
        String email = snapshot.getString("email");
        String uid = snapshot.getString("uid");
        Date createdAt = snapshot.getDate("createdAt");

        return buildPdf(name, email, uid, createdAt);
    }

    private DocumentSnapshot getUserDocument(String userId) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
            DocumentSnapshot snapshot = future.get();

            if (!snapshot.exists()) {
                throw new RuntimeException("User not found");
            }

            return snapshot;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user document", e);
        }
    }

    private byte[] buildPdf(String name, String email, String uid, Date createdAt) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);
            document.setMargins(50, 40, 40, 40);

            addTitle(document);
            addLine(document);
            addUserInfoTable(document, name, email, uid, createdAt);
            addFooter(document);


            WatermarkService.addImageWatermark(pdf);

            document.close();
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating profile PDF", e);
        }
    }

    private void addTitle(Document document) {
        Paragraph title = new Paragraph("Informações do Usuário")
                .setFontSize(22)
                .setBold()
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);
    }

    private void addLine(Document document) {
        SolidLine line = new SolidLine(0.5f);
        line.setColor(ColorConstants.LIGHT_GRAY);
        document.add(new LineSeparator(line).setMarginBottom(25));
    }

    private void addUserInfoTable(Document document, String name, String email, String uid, Date createdAt) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Table table = new Table(UnitValue.createPercentArray(new float[]{1.3f, 3f}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        table.addCell(createLabelCell("Nome"));
        table.addCell(createValueCell(name));

        table.addCell(createLabelCell("E-mail"));
        table.addCell(createValueCell(email));

        table.addCell(createLabelCell("UID"));
        table.addCell(createValueCell(uid));

        table.addCell(createLabelCell("Criado em"));
        table.addCell(createValueCell(createdAt != null ? formatter.format(createdAt) : "N/A"));

        document.add(table);
    }

    private void addFooter(Document document) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Paragraph footer = new Paragraph("Gerado em " + formatter.format(new Date()))
                .setFontSize(9)
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30);
        document.add(footer);
    }

    private com.itextpdf.layout.element.Cell createLabelCell(String content) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(content).setFontColor(ColorConstants.GRAY).setFontSize(11))
                .setPadding(6)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private com.itextpdf.layout.element.Cell createValueCell(String content) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(content).setFontSize(12).setBold())
                .setPadding(6)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }
}