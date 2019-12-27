package com.ortiz.userprofilestore.data.repository;

import com.ortiz.userprofilestore.data.model.UserModel;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCouchbaseSortingRepository<UserModel, String> {

    @Query("#{#n1ql.selectEntity} WHERE docType = 'user' AND ANY e IN pointsOfContact.emailAddresses SATISFIES e.email = $1 AND e.provider = $2 END")
    Mono<UserModel> findUserModelByEmailAddress(String emailAddress, String provider);

    @Query("#{#n1ql.selectEntity} WHERE docType = 'user' AND ANY p IN pointsOfContact.phoneNumbers SATISFIES `p`.`number` = $1 end")
    Mono<UserModel> findUserModelByPhoneNumber(String phoneNumber);
}
