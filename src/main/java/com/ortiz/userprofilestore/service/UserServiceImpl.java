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
                .flatMap(user -> userRepository.existsById(user))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("The user " + userName + " already exists"));
                    }
                    //Collect Email Addresses
                    Set<EmailAddress> emailAddresses = pointsOfContact.getEmailAddresses();

                    //Collect Phone Numbers
                    Set<String> phoneNumbers = pointsOfContact.getPhoneNumbers().stream()
                            .map(PhoneNumber::getNumber)
                            .collect(Collectors.toSet());

                    //Validate all emails and phone numbers provided are unique
                    Mono<Boolean> usersExistWithEmail = Flux.fromIterable(emailAddresses)
                            .flatMap(email -> isUniqueEmailAddress(email.getEmail(), email.getProvider()))
                            .all(unique -> unique.equals(Boolean.TRUE));

                    Mono<Boolean> usersExistWithPhoneNumber = Flux.fromIterable(phoneNumbers)
                            .flatMap(this::isUniquePhoneNumber)
                            .all(unique -> unique.equals(Boolean.TRUE));

                    //Wait for both to finish
                    return Mono.zip(usersExistWithEmail, usersExistWithPhoneNumber, (T1, T2) -> T1 && T2);
                }).flatMap(valid -> {
                    if (valid) {
                        return userRepository.save(new UserModel(userName, passwordEncoder.encode(password), firstName, lastName, role, pointsOfContact))
                                .map(User::new);
                    } else {
                        return Mono.error(new IllegalArgumentException("Please provide valid Email Addresses and Phone Numbers"));
                    }
                });
    }

    @Override
    public Mono<User> updateUserPointsOfContact(String userName, PointsOfContact pointsOfContact) {
        return Mono.just(userName)
                .flatMap(u -> userRepository.findById(u))
                .flatMap(user -> {
                    if (user == null) {
                        return Mono.error(new IllegalArgumentException("The user " + userName + " does not exist"));
                    }

                    //Collect Email Addresses
                    Set<EmailAddress> emailAddresses = pointsOfContact.getEmailAddresses();

                    //Collect Phone Numbers
                    Set<String> phoneNumbers = pointsOfContact.getPhoneNumbers().stream()
                            .map(PhoneNumber::getNumber)
                            .collect(Collectors.toSet());

                    //Validate all emails and phone numbers provided are unique
                    Mono<Boolean> allUniqueEmails = Flux.fromIterable(emailAddresses)
                            .flatMap(email -> isUniqueEmailAddress(email.getEmail(), email.getProvider()))
                            .all(unique -> unique.equals(Boolean.TRUE));
                    Mono<Boolean> allUniquePhoneNumbers = Flux.fromIterable(phoneNumbers)
                            .flatMap(this::isUniquePhoneNumber)
                            .all(unique -> unique.equals(Boolean.TRUE));

                    //Wait for both to finish
                    return Mono.zip(allUniqueEmails, allUniquePhoneNumbers, (T1, T2) -> T1 && T2)
                            .flatMap(valid -> {
                                if (valid) {
                                    user.getPointsOfContact().getEmailAddresses().addAll(pointsOfContact.getEmailAddresses());
                                    user.getPointsOfContact().getPhoneNumbers().addAll(pointsOfContact.getPhoneNumbers());
                                    user.getPointsOfContact().getPhysicalAddresses().addAll(pointsOfContact.getPhysicalAddresses());
                                    return userRepository.save(user);

                                }
                                return Mono.error(new IllegalArgumentException("Unable to update user \"" + userName + "\": please provide unique emails and phone numbers"));
                            });
                })
                .map(User::new);
    }

    @Override
    public Mono<Void> deleteUser(String userName) {
        return Mono.just(userName)
                .flatMap(user -> {
                    try {
                        return userRepository.deleteById(user);
                    } catch (DocumentDoesNotExistException e) {
                        return Mono.error(new IllegalArgumentException("Cannot delete user " + user + ". User does not exist"));
                    }
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
