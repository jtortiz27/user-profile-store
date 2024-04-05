package com.ortiz.userprofilestore.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import java.util.List;

@Configuration
@EnableReactiveCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Autowired
    private CouchbaseProperties couchbaseProperties;

    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
    @Override
    public String typeKey() {
        return "docType";
    }

//    @Override
//    public CouchbaseEnvironment couchbaseEnvironment() {
//        return DefaultCouchbaseEnvironment.builder()
//                .kvTimeout(0)
//                .viewTimeout(0)
//                .queryTimeout(0)
//                .operationTracingEnabled(false)
//                .orphanResponseReportingEnabled(false)
//                .build();
//    }
}
