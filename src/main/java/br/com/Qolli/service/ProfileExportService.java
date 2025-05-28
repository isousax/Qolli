package br.com.Qolli.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ProfileExportService {

    public byte[] generateProfilePdf(String userId) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
            DocumentSnapshot snapshot = future.get();

            if (!snapshot.exists()) {
                throw new RuntimeException("User not found");
            }

            String name = snapshot.getString("name");
            String email = snapshot.getString("email");
            String uid = snapshot.getString("uid");
            Date createdAt = snapshot.getDate("createdAt");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Perfil do Usuário").setBold().setFontSize(14));
            document.add(new Paragraph("Nome: " + name));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("UID: " + uid));
            document.add(new Paragraph("Criado em: " + (createdAt != null ? formatter.format(createdAt) : "N/A")));

            document.close();
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating profile PDF", e);
        }
    }
}
