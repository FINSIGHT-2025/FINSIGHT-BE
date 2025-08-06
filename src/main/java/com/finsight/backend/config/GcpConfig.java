package com.finsight.backend.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.cloud.language.v2.LanguageServiceClient;
import com.google.cloud.language.v2.LanguageServiceSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@PropertySource({"classpath:/application.properties"})
@RequiredArgsConstructor
public class GcpConfig {

    @Value("${gcp.credentials.location}")
    private String credentialsLocation;

    private final ResourceLoader resourceLoader;

    @Bean
    public LanguageServiceClient languageServiceClient() throws IOException {
        Resource resource = resourceLoader.getResource(credentialsLocation);
        InputStream credentialsStream = resource.getInputStream();

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return LanguageServiceClient.create(settings);
    }
}
