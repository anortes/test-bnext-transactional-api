package com.bnext.api;

import com.bnext.api.repositories.ContactRepository;
import com.bnext.api.repositories.UserRepository;
import com.bnext.api.repositories.models.ContactModel;
import com.bnext.api.repositories.models.UserModel;
import com.bnext.api.services.impl.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));

        List<UserModel> users = userIds.stream()
                .map(userId -> new UserModel(
                        userId,
                        "User " + userId,
                        "Test " + userId,
                        "user" + userId + "@gmail.com"))
                .collect(Collectors.toList());

        List<UserModel> savedUsers = userRepository.saveAll(users);

        ArrayList<ContactModel> contacts = new ArrayList<>();
        contacts.add(new ContactModel(
                sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME),
                savedUsers.get(0).getId(),
                "Contact 1",
                "Test 1",
                "678123456"
        ));
        contacts.add(new ContactModel(
                sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME),
                savedUsers.get(0).getId(),
                "Contact 2",
                "Test 2",
                "678123457"
        ));
        contacts.add(new ContactModel(
                sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME),
                savedUsers.get(0).getId(),
                "Contact 3",
                "Test 3",
                "678123458"
        ));
        contacts.add(new ContactModel(
                sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME),
                savedUsers.get(1).getId(),
                "Contact 4",
                "Test 4",
                "678123457"
        ));
        contacts.add(new ContactModel(
                sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME),
                savedUsers.get(1).getId(),
                "Contact 5",
                "Test 5",
                "678123459"
        ));

        contactRepository.saveAll(contacts);
    }
}
