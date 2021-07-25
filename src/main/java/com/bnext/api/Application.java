package com.bnext.api;

import com.bnext.api.repositories.ContactRepository;
import com.bnext.api.repositories.UserRepository;
import com.bnext.api.services.impl.SequenceGeneratorService;
import com.bnext.api.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        CommonUtils.initApplicationData(userRepository, contactRepository, sequenceGeneratorService);
    }
}
