package br.com.Qolli.service.fetcher;

import br.com.Qolli.dto.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class UserProfileFetcher {
    public UserProfile fetch(String userId) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
            DocumentSnapshot snapshot = future.get();

            if (!snapshot.exists()) {
                throw new RuntimeException("User not found");
            }

            return new UserProfile(
                    snapshot.getString("name"),
                    snapshot.getString("email"),
                    snapshot.getString("uid"),
                    snapshot.getDate("createdAt")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user document", e);
        }
    }
}
