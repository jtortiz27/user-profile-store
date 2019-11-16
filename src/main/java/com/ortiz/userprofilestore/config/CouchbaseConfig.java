package com.ortiz.userprofilestore.config;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import java.util.List;

@ConfigurationProperties
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Autowired
    private CouchbaseProperties couchbaseProperties;

    @Value("${spring.couchbase.bootstrap-hosts}")
    private List<String> bootStrapHosts;

    @Value("${spring.couchbase.bucketName}")
    private String bucketName;

    @Value("${spring.couchbase.bucketPassword}")
    private String bucketPassword;

    @Override
    public List<String> getBootstrapHosts() {
        return couchbaseProperties.getBootstrapHosts();
    }

    @Override
    public String getBucketName() {
        return couchbaseProperties.getBucket().getName();
    }

    @Override
    public String getBucketPassword() {
        return couchbaseProperties.getBucket().getPassword();
    }

    @Override
    public String typeKey() {
        return "docType";
    }

    @Bean
    public CouchbaseEnvironment getCouchbaseEnvironment(CouchbaseProperties couchbaseProperties) {
        return DefaultCouchbaseEnvironment.builder()
                .connectTimeout(0)
                .kvTimeout(0)
                .viewTimeout(0)
                .queryTimeout(0)
                .sslEnabled(true)
                .build();
    }
}
