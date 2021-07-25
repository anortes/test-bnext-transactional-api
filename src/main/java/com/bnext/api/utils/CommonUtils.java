package com.bnext.api.utils;

import com.bnext.api.repositories.ContactRepository;
import com.bnext.api.repositories.UserRepository;
import com.bnext.api.repositories.models.ContactModel;
import com.bnext.api.repositories.models.UserModel;
import com.bnext.api.services.impl.SequenceGeneratorService;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de utilidades. MImplementación de métodos comunes de la aplicación.
 */
@UtilityClass
public class CommonUtils {

    /**
     * Método que genera algunos usuarios y sus contactos en BD al ejecutar la aplicación,
     * para tener al menos algún juego de datos al inicio.
     * @param userRepository Repositorio de usuarios.
     * @param contactRepository Repositorio de contactos
     * @param sequenceGeneratorService Generador de ids.
     */
    public void initApplicationData(UserRepository userRepository, ContactRepository contactRepository,
                         SequenceGeneratorService sequenceGeneratorService) {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        List<Long> userIds = new ArrayList<>();
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        userIds.add(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));

        List<UserModel> users = initDataUsers(userIds);

        List<UserModel> savedUsers = userRepository.saveAll(users);

        List<Long> contactsIds = new ArrayList<>();
        contactsIds.add(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));
        contactsIds.add(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));
        contactsIds.add(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));
        contactsIds.add(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));
        contactsIds.add(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));

        contactRepository.saveAll(initDataContacts(contactsIds, savedUsers));
    }

    /**
     * Método para obtener un listado de usuarios mockeados.
     * @return List<UserModel> Listado de usuarios mockeados.
     */
    public List<UserModel> initUsersMockData() {

        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        userIds.add(2L);
        userIds.add(3L);

        return initDataUsers(userIds);
    }

    /**
     * Método para obtener listados de contactos mockeados según usuarios mockeados.
     * @param savedUsers Listado de usuarios mockeados para asignarle algunos contactos.
     * @return Listado de contactos mockeados.
     */
    public List<ContactModel> initContactsMockData(List<UserModel> savedUsers) {

        List<Long> contactsIds = new ArrayList<>();
        contactsIds.add(1L);
        contactsIds.add(2L);
        contactsIds.add(3L);
        contactsIds.add(4L);
        contactsIds.add(5L);

        return initDataContacts(contactsIds, savedUsers);
    }

    /**
     * Método que asigna valores mockeados a usuarios.
     * @param userIds Ids de usuarios que se van a mockear.
     * @return Listado de usuarios mockeados.
     */
    public List<UserModel> initDataUsers(List<Long> userIds) {

        return userIds.stream()
                .map(userId -> new UserModel(
                        userId,
                        "User " + userId,
                        "Test " + userId,
                        "user" + userId + "@gmail.com"))
                .collect(Collectors.toList());
    }

    /**
     * Método que asigna valores mockeados a contactos.
     * @param contactsIds Ids de los contactos a mockear.
     * @param savedUsers Usuarios mockeados a asignar contactos mockeados.
     * @return Listado de contactos mockeados.
     */
    public List<ContactModel> initDataContacts(List<Long>  contactsIds, List<UserModel> savedUsers) {

        List<ContactModel> contacts = new ArrayList<>();
        contacts.add(new ContactModel(
                contactsIds.get(0),
                savedUsers.get(0).getId(),
                "Contact 1",
                "Test 1",
                "678123456"
        ));
        contacts.add(new ContactModel(
                contactsIds.get(1),
                savedUsers.get(0).getId(),
                "Contact 2",
                "Test 2",
                "678123457"
        ));
        contacts.add(new ContactModel(
                contactsIds.get(2),
                savedUsers.get(0).getId(),
                "Contact 3",
                "Test 3",
                "678123458"
        ));
        contacts.add(new ContactModel(
                contactsIds.get(3),
                savedUsers.get(1).getId(),
                "Contact 4",
                "Test 4",
                "678123457"
        ));
        contacts.add(new ContactModel(
                contactsIds.get(4),
                savedUsers.get(1).getId(),
                "Contact 5",
                "Test 5",
                "678123459"
        ));
        return contacts;
    }
}
