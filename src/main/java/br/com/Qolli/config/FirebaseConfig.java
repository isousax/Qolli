package br.com.Qolli.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            String firebaseEnv = System.getenv("FIREBASE_CONFIG");

            if (firebaseEnv == null || firebaseEnv.isEmpty()) {
                throw new IllegalStateException("FIREBASE_CONFIG environment variable is not set or empty");
            }

            InputStream serviceAccount = new ByteArrayInputStream(firebaseEnv.getBytes(StandardCharsets.UTF_8));
            System.out.println("Starting Firebase with environment variable.");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }
}