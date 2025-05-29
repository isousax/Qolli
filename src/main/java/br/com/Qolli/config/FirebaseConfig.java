package br.com.Qolli.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount;
            String firebaseEnv = System.getenv("FIREBASE_CONFIG");

            if (firebaseEnv != null && !firebaseEnv.isEmpty()) {
                // PROD
                serviceAccount = new ByteArrayInputStream(firebaseEnv.getBytes(StandardCharsets.UTF_8));
                System.out.println("Starting Firebase with environment variable.");
            } else {
                // DEV
                serviceAccount = new FileInputStream(firebaseConfigPath);
                System.out.println("Starting Firebase with local file.");
            }

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