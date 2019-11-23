package com.ortiz.userprofilestore.service;

import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.model.UserModel;
import com.ortiz.userprofilestore.data.repository.UserRepository;
import com.ortiz.userprofilestore.service.model.EmailAddress;
import com.ortiz.userprofilestore.service.model.PhoneNumber;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Flux<User> retrieveAllUsers() {
        return userRepository.findAll()
                .map(User::new);
    }

    @Override
    public Mono<User> retrieveUserByUserName(String userName) {
        return userRepository.findById(userName)
                .map(User::new);
    }

    @Override
    public Mono<User> createUser(String userName, String password, String firstName, String lastName, Role role, PointsOfContact pointsOfContact) {
        Boolean userExists = userRepository.existsById(userName).block();

        if (userExists != null && userExists) {
            return Mono.error(DocumentAlreadyExistsException::new);
        }

        return userRepository.findUserModelByEmailAddress(pointsOfContact.getEmailAddresses().stream().findFirst().get().getEmail())
                .switchIfEmpty(userRepository.save(new UserModel(userName, passwordEncoder.encode(password), firstName, lastName, role, pointsOfContact)))
                .map(User::new);
    }

    @Override
    public Mono<User> updateUserPointsOfContact(String userName, PointsOfContact pointsOfContact) {
        Boolean userExists = userRepository.existsById(userName).block();

        if (userExists != null && !userExists) {
            return Mono.error(new IllegalArgumentException("Error retrieving user to update"));
        }

        //Collect Email Addresses
        Mono<Set<String>> emailAddressMono = Mono.just(pointsOfContact.getEmailAddresses().stream()
                .map(EmailAddress::getEmail)
                .collect(Collectors.toSet()));
        //Collect Phone Numbers
        Mono<Set<String>> phoneNumberMono = Mono.just(pointsOfContact.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toSet()));

        //Validate all emails and phone numbers provided are unique
        Boolean valid = Mono.zip(emailAddressMono, phoneNumberMono, (emailAddresses, phoneNumbers) -> {
            Mono<Boolean> usersExistWithEmail = Flux.fromIterable(emailAddresses)
                    .flatMap(this::isUniqueEmailAddress)
                    .all(Boolean::booleanValue);
            Mono<Boolean> usersExistWithPhoneNumber = Flux.fromIterable(phoneNumbers)
                    .flatMap(this::isUniquePhoneNumber)
                    .all(Boolean::booleanValue);

            //Wait for both to finish
            return usersExistWithEmail.zipWith(usersExistWithPhoneNumber, (T1, T2) -> {
                return T1 && T2;
            }).block();
        }).block();

        if (valid != null && valid) {
            return userRepository.findById(userName)
                    .map(userModel -> {
                        userModel.getPointsOfContact().getEmailAddresses().addAll(pointsOfContact.getEmailAddresses());
                        userModel.getPointsOfContact().getPhoneNumbers().addAll(pointsOfContact.getPhoneNumbers());
                        userModel.getPointsOfContact().getPhysicalAddresses().addAll(pointsOfContact.getPhysicalAddresses());
                        return userRepository.save(userModel)
                                .switchIfEmpty(Mono.error(DocumentDoesNotExistException::new))
                                .block();
                    })
                    .map(User::new);
        }
        return Mono.error(new IllegalArgumentException("Please enter unique email addresses and phone numbers"));
    }

    private Mono<Boolean> isUniqueEmailAddress(String emailAddress) {
        return userRepository.findUserModelByEmailAddress(emailAddress)
                .map(userModel -> {
                    if (userModel == null) {
                        log.info(emailAddress);
                        return Boolean.TRUE;
                    } else {
                        log.info(userModel.toString());
                        return Boolean.FALSE;
                    }
                });
    }

    private Mono<Boolean> isUniquePhoneNumber(String phoneNumber) {
        return userRepository.findUserModelByPhoneNumber(phoneNumber)
                .map(userModel -> {
                    if (userModel == null) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                });
    }
}
