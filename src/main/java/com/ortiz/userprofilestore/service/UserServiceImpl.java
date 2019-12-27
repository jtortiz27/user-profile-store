package com.ortiz.userprofilestore.service;

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

import java.util.HashSet;
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
        return Mono.just(userName)
                .flatMap(user -> userRepository.existsById(user).switchIfEmpty(Mono.defer(() -> {

                    //Collect Email Addresses
                    Mono<Set<EmailAddress>> emailAddressMono = Mono.just(new HashSet<>(pointsOfContact.getEmailAddresses()));

                    //Collect Phone Numbers
                    Mono<Set<String>> phoneNumberMono = Mono.just(pointsOfContact.getPhoneNumbers().stream()
                            .map(PhoneNumber::getNumber)
                            .collect(Collectors.toSet()));

                    //Validate all emails and phone numbers provided are unique
                    return Mono.zip(emailAddressMono, phoneNumberMono, (emailAddresses, phoneNumbers) -> {

                        Mono<Boolean> usersExistWithEmail = Flux.fromIterable(emailAddresses)
                                .flatMap(email -> isUniqueEmailAddress(email.getEmail(), email.getProvider()))
                                .all(Boolean::booleanValue);

                        Mono<Boolean> usersExistWithPhoneNumber = Flux.fromIterable(phoneNumbers)
                                .flatMap(this::isUniquePhoneNumber)
                                .all(Boolean::booleanValue);

                        //Wait for both to finish
                        return Mono.zip(usersExistWithEmail, usersExistWithPhoneNumber, (T1, T2) -> {
                            return T1 && T2;
                        });
                    }).block();
                })))
                .flatMap(userExists -> {
                    if (userExists != null && userExists) {
                        return userRepository.save(new UserModel(userName, passwordEncoder.encode(password), firstName, lastName, role, pointsOfContact))
                                .map(User::new);
                    } else {
                        return Mono.error(new IllegalArgumentException("The user already exists"));
                    }
                });
    }

    @Override
    public Mono<User> updateUserPointsOfContact(String userName, PointsOfContact pointsOfContact) {
        return Mono.just(userName)
                .flatMap(user -> {
                    userRepository.existsById(user).switchIfEmpty(Mono.defer(() -> Mono.error(DocumentDoesNotExistException::new)));

                    //Collect Email Addresses
                    Mono<Set<EmailAddress>> emailAddressMono = Mono.just(new HashSet<>(pointsOfContact.getEmailAddresses()));

                    //Collect Phone Numbers
                    Mono<Set<String>> phoneNumberMono = Mono.just(pointsOfContact.getPhoneNumbers().stream().map(PhoneNumber::getNumber).collect(Collectors.toSet()));

                    //Validate all emails and phone numbers provided are unique
                    Boolean valid = Mono.zip(emailAddressMono, phoneNumberMono, (emailAddresses, phoneNumbers) -> {
                        Mono<Boolean> usersExistWithEmail = Flux.fromIterable(emailAddresses)
                                .flatMap(email -> isUniqueEmailAddress(email.getEmail(), email.getProvider()))
                                .all(Boolean::booleanValue);
                        Mono<Boolean> usersExistWithPhoneNumber = Flux.fromIterable(phoneNumbers)
                                .flatMap(this::isUniquePhoneNumber)
                                .all(Boolean::booleanValue);

                        //Wait for both to finish
                        return usersExistWithEmail.zipWith(usersExistWithPhoneNumber, (T1, T2) -> T1 && T2).block();
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
                });
    }

    private Mono<Boolean> isUniqueEmailAddress(String emailAddress, String provider) {
        return userRepository.findUserModelByEmailAddress(emailAddress, provider)
                .map(userModel -> {
                    if (userModel == null) {
                        log.info(emailAddress);
                        return Boolean.TRUE;
                    } else {
                        log.info("Attempted to update emailAddress {} failed because there is already a user with this email address: {}", emailAddress, userModel.toString());
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
                        log.info("Attempted to update phoneNumber {} failed because there is already a user with this phone number: {}", phoneNumber, userModel.toString());
                        return Boolean.FALSE;
                    }
                });
    }
}
