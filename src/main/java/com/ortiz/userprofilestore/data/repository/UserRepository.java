package com.ortiz.userprofilestore.data.repository;

import com.ortiz.userprofilestore.data.model.UserModel;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCouchbaseSortingRepository<UserModel, String> {

    @Query("SELECT * FROM users WHERE docType = 'user' and any email in pointsOfContact.emailAddresses.email satisfies $1")
    Mono<UserModel> findUserModelByEmailAddress(String emailAddress);
}
