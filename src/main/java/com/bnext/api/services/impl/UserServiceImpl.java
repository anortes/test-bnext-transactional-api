package com.bnext.api.services.impl;

import com.bnext.api.dtos.*;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import com.bnext.api.mappers.ContactMapper;
import com.bnext.api.mappers.UserMapper;
import com.bnext.api.repositories.ContactRepository;
import com.bnext.api.repositories.UserRepository;
import com.bnext.api.repositories.models.ContactModel;
import com.bnext.api.repositories.models.UserModel;
import com.bnext.api.services.UserService;
import com.bnext.api.services.ValidatePhoneService;
import com.bnext.api.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase donde se implementan todos los servicios relacionados con usuarios y sus contactos
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidatePhoneService validatePhoneService;

    /**
     * Servicio que devuelve todos los usuarios de la BD.
     * @return List<UserResponseDto>
     */
    @Override
    @Transactional(readOnly=true)
    public List<UserResponseDto> findAllUsers() {
        List<UserModel> usersModel = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<UserResponseDto>(usersModel.size());
        usersModel.stream().forEach(userModel -> {
            userResponseDtos.add(UserMapper.INSTANCE.UserModelToUserResponseDto(userModel));
        });
        return userResponseDtos;
    }

    /**
     * Servicio que crea el usuario recibido por parámetro de entrada en BD y lo devuelve ya con su id.
     * @param userRequestDto Objeto con los datos del usuario a crear.
     * @return UserResponseDto
     */
    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        userRequestDto.setId(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        UserModel userModel = UserMapper.INSTANCE.UserRequestDtoToUserModel(userRequestDto);
        userModel = userRepository.save(userModel);
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    /**
     * Servicio que busca un usuario a través del id recibido por parámetro de entrada y lo devuelve si existe.
     * @param userId Id del usuario a buscar
     * @return UserResponseDto
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @Override
    public UserResponseDto findByUserId(Long userId) throws ResourceNotFoundException {
        UserModel userModel = UserModel.builder().id(userId).build();
        userModel = userRepository.findById(userModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.USER_NOT_FOUND_MESSAGE + userId));
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    /**
     * Servicio que recibe como parámetros de entrada el id y los nuevos datos de un usuario.
     * Si el id de usuario existe, actualiza el usuario con los nuevos datos y devuelve el usuario.
     * @param userId Id del usuario a actualizar
     * @param userRequestDto Objeto con los nuevos datos del usuario a actualizar.
     * @return UserResponseDto
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @Override
    @Transactional
    public UserResponseDto saveUser(Long userId, UserRequestDto userRequestDto) throws ResourceNotFoundException {
        findByUserId(userId);
        UserModel userModel = UserMapper.INSTANCE.UserRequestDtoToUserModel(userRequestDto);
        userModel.setId(userId);
        userModel = userRepository.save(userModel);
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    /**
     * Servicio que recibe el id de un usuario. Si el id existe, se eliminan sus contactos y después el usuario.
     * @param userId Id del usuario a eliminar.
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        UserResponseDto userResponseDto = findByUserId(userId);
        deleteContactsByUserId(userId);
        UserModel userModel = UserMapper.INSTANCE.UserResponseDtoToUserModel(userResponseDto);
        userRepository.delete(userModel);
    }

    /**
     * Servicio que recibe el id de un usuario. Si el id existe, se eliminan todos los contactos de ese usuario.
     * @param userId Id del usuario al cuál se le van a eliminar todos los contactos.
     */
    @Override
    @Transactional
    public void deleteContactsByUserId(Long userId) {
        contactRepository.deleteContactsByUser(userId);
    }

    /**
     * Servicio que recibe un id de usuario y un listado de contactos. Se valida si el usuario existe
     * y si los números de teléfono son válidos (consumiendo una API externa para ello).
     * Si el usuario existe y los números de teléfono son válidos, se eliminan los contactos existentes para ese usuario
     * y se insertan los nuevos contactos.
     * @param userId Id del usuario al cual se le van a actualizar todos sus contactos
     * @param contactsRequestDto Listado de nuevos contactos del usuario.
     * @return List<ContactResponseDto>
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     * @throws InvalidDataException Si alguno de los números de teléfono no es válido.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ContactResponseDto> saveUserContacts(Long userId, List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException {
        List<ServiceResponseDto> serviceResponseDtos = contactsRequestDto.stream().map(contact -> validatePhoneService.
                validatePhone(ServiceRequestDto.builder().number(contact.getPhone()).build())).collect(Collectors.toList());

        if (!serviceResponseDtos.stream().anyMatch(responseDto -> responseDto.isValid())) {
            throw new InvalidDataException(CommonConstants.INVALID_PHONE_MESSAGE);
        }

        findByUserId(userId);
        deleteContactsByUserId(userId);

        List<ContactModel> contactModels = contactsRequestDto.stream()
                .map(contact -> {
                    contact.setUserId(userId);
                    contact.setId(sequenceGeneratorService.generateSequence(ContactModel.SEQUENCE_NAME));
                    return ContactMapper.INSTANCE.ContactRequestDtoToContactModel(contact);
                })
                .collect(Collectors.toList());

        List<ContactModel> contactModelList = contactRepository.saveAll(contactModels);

        return contactModelList.stream()
                .map(ContactMapper.INSTANCE::ContactModelToContactResponseDto)
                .collect(Collectors.toList());

    }

    /**
     * Servicio que devuelve todos los contactos de un usuario, si el id del usuario proporcionado existe.
     * @param userId Id del usuario para buscar todos sus contactos.
     * @return List<ContactResponseDto>
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContactResponseDto> findContactsByUserId(Long userId) throws ResourceNotFoundException {

        UserResponseDto user = findByUserId(userId);

        List<ContactModel> contacts = contactRepository.findContactsByUser(user.getId());

        return contacts.stream()
                .map(ContactMapper.INSTANCE::ContactModelToContactResponseDto)
                .collect(Collectors.toList());

    }

    /**
     * Servicio que obtiene los contactos comunes de dos usuarios cuyos ids son recibidos como entrada
     * si los dos usuarios existen.
     * @param userId1 Id de un usuario para buscar todos sus contactos
     * @param userId2 Id de otro usuario para buscar todos sus contactos.
     * @return List<ContactResponseDto>
     * @throws ResourceNotFoundException Si alguno de los dos ids de usuario no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContactResponseDto> getCommonsContacts(Long userId1, Long userId2)
            throws ResourceNotFoundException {
        List<ContactResponseDto> contactsByUserId1 = findContactsByUserId(userId1);
        List<String> phonesByUserId2 = findContactsByUserId(userId2).stream()
                .map(ContactResponseDto::getPhone)
                .collect(Collectors.toList());

        return contactsByUserId1.stream()
                .filter(c -> phonesByUserId2.contains(c.getPhone()))
                .collect(Collectors.toList());
    }
}
