package br.com.Qolli.service.fetcher;

import br.com.Qolli.dto.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageFetcher {
    public List<Message> fetchMessages(String conversationKey) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> future = db.collection("conversations")
                    .document(conversationKey)
                    .collection("messages")
                    .orderBy("timestamp")
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Message> messages = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
                messages.add(new Message(
                        doc.getString("fromId"),
                        doc.getString("toId"),
                        doc.getString("text"),
                        doc.getDate("timestamp")
                ));
            }
            return messages;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch messages", e);
        }
    }
}
