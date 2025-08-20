package com.finsight.backend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class EsConfig {

    @Value("${elasticsearch.host}")       private String host;
    @Value("${elasticsearch.username}")   private String username;
    @Value("${elasticsearch.password}")   private String password;
    @Value("${elasticsearch.caCertPath}") private String caCert;

    private final ResourceLoader resourceLoader;

    public EsConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {
        SSLContext ssl = buildSslContextFrom(caCert);

        CredentialsProvider creds = new BasicCredentialsProvider();
        creds.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        HttpHost httpHost = HttpHost.create(host);

        RestClientBuilder builder = RestClient.builder(httpHost)
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(ssl)
                        .setDefaultCredentialsProvider(creds)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                )
                .setRequestConfigCallback((RequestConfig.Builder rc) ->
                        rc.setConnectTimeout(5_000)
                                .setSocketTimeout(10_000)
                );

        RestClient low = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(low, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    private SSLContext buildSslContextFrom(String location) throws Exception {
        Resource res = resourceLoader.getResource(location);
        if (!res.exists()) throw new IllegalArgumentException("CA cert not found: " + location);

        X509Certificate caCert;
        try (InputStream is = res.getInputStream()) {
            caCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        }

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("ca", caCert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(null, tmf.getTrustManagers(), null);
        return ssl;
    }
}
