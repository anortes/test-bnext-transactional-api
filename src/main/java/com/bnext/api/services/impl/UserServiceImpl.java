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

    @Override
    @Transactional(readOnly=true)
    public List<UserResponseDto> finAllUsers() {
        List<UserModel> usersModel = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<UserResponseDto>(usersModel.size());
        usersModel.stream().forEach(userModel -> {
            userResponseDtos.add(UserMapper.INSTANCE.UserModelToUserResponseDto(userModel));
        });
        return userResponseDtos;
    }

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        userRequestDto.setId(sequenceGeneratorService.generateSequence(UserModel.SEQUENCE_NAME));
        UserModel userModel = UserMapper.INSTANCE.UserRequestDtoToUserModel(userRequestDto);
        userModel = userRepository.save(userModel);
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    @Override
    public UserResponseDto findByUserId(Long userId) throws ResourceNotFoundException {
        UserModel userModel = UserModel.builder().id(userId).build();
        userModel = userRepository.findById(userModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.USER_NOT_FOUND_MESSAGE + userId));
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    @Override
    @Transactional
    public UserResponseDto saveUser(Long userId, UserRequestDto userRequestDto) throws ResourceNotFoundException {
        findByUserId(userId);
        UserModel userModel = UserMapper.INSTANCE.UserRequestDtoToUserModel(userRequestDto);
        userModel.setId(userId);
        userModel = userRepository.save(userModel);
        return UserMapper.INSTANCE.UserModelToUserResponseDto(userModel);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        UserResponseDto userResponseDto = findByUserId(userId);
        UserModel userModel = UserMapper.INSTANCE.UserResponseDtoToUserModel(userResponseDto);
        userRepository.delete(userModel);
    }

    @Override
    @Transactional
    public void deleteContactsByUserId(Long userId) throws ResourceNotFoundException {
        findByUserId(userId);
        contactRepository.deleteContactsByUser(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ContactResponseDto> saveUserContacts(Long userId, List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException {
        List<ServiceResponseDto> serviceResponseDtos = contactsRequestDto.stream().map(contact -> {
            return validatePhoneService.
                    validatePhone(ServiceRequestDto.builder().number(contact.getPhone()).build());
        }).collect(Collectors.toList());

        if (!serviceResponseDtos.stream().anyMatch(responseDto -> responseDto.isValid())) {
            throw new InvalidDataException(CommonConstants.INVALID_PHONE_MESSAGE);
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponseDto> findContactsByUserId(Long userId) throws ResourceNotFoundException {

        UserResponseDto user = findByUserId(userId);

        List<ContactModel> contacts = contactRepository.findContactsByUser(user.getId());

        return contacts.stream()
                .map(ContactMapper.INSTANCE::ContactModelToContactResponseDto)
                .collect(Collectors.toList());

    }


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
