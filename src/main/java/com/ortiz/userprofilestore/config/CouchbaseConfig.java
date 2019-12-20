package com.ortiz.userprofilestore.config;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import java.util.List;

@Configuration
@EnableReactiveCouchbaseRepositories
public class CouchbaseConfig extends AbstractReactiveCouchbaseConfiguration {

    @Autowired
    private CouchbaseProperties couchbaseProperties;

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

    @Override
    public CouchbaseEnvironment couchbaseEnvironment() {
        return DefaultCouchbaseEnvironment.builder()
                .kvTimeout(0)
                .viewTimeout(0)
                .queryTimeout(0)
                .operationTracingEnabled(false)
                .orphanResponseReportingEnabled(false)
                .build();
    }
}
