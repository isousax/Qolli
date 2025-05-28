package br.com.Qolli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageExportService {
    public byte[] generateMessagePdf(String conversationKey) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("conversations")
                    .document(conversationKey)
                    .collection("messages")
                    .orderBy("timestamp")
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<String> lines = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
                String from = doc.getString("fromId");
                String to = doc.getString("toId");
                String text = doc.getString("text");
                lines.add(String.format("De: %s  Para: %s\nMensagem: %s\n", from, to, text));
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            for (String line : lines) {
                document.add(new Paragraph(line));
            }

            WatermarkService.addImageWatermark(pdf);

            document.close();
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF of messages", e);
        }
    }

    public byte[] generateMessageJson(String conversationKey) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("conversations")
                    .document(conversationKey)
                    .collection("messages")
                    .orderBy("timestamp")
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Map<String, Object>> messageList = new ArrayList<>();
            for (DocumentSnapshot doc : documents) {
                messageList.add(doc.getData());
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(messageList);

        } catch (Exception e) {
            throw new RuntimeException("Error exporting messages", e);
        }
    }
}