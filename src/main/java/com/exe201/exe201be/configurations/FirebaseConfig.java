package com.exe201.exe201be.configurations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Sử dụng getResourceAsStream thay vì FileInputStream
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("exe201-c1658-firebase-adminsdk-k6tip-1b5a951916.json");

        if (serviceAccount == null) {
            throw new FileNotFoundException("File not found: exe201-c1658-firebase-adminsdk-k6tip-1b5a951916.json");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("exe201-c1658.appspot.com") // Thay <your-project-id> bằng ID dự án của bạn
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
