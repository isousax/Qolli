package br.com.Qolli.service.export;

import br.com.Qolli.dto.Message;
import br.com.Qolli.service.fetcher.MessageFetcher;
import br.com.Qolli.service.pdf.MessagePdfBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageExportService {
    private final MessageFetcher messageFetcher;
    private final MessagePdfBuilder messagePdfBuilder;

    public MessageExportService(MessageFetcher messageFetcher, MessagePdfBuilder messagePdfBuilder) {
        this.messageFetcher = messageFetcher;
        this.messagePdfBuilder = messagePdfBuilder;
    }

    public byte[] generateMessagePdf(String conversationKey) {
        List<Message> messages = messageFetcher.fetchMessages(conversationKey);
        return messagePdfBuilder.build(messages);
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