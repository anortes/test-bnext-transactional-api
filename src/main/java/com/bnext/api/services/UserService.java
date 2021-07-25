package com.bnext.api.services;

import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserService {

    List<UserResponseDto> findAllUsers();

    UserResponseDto addUser(UserRequestDto userRequestDto);

    UserResponseDto findByUserId(Long userId) throws ResourceNotFoundException;

    UserResponseDto saveUser(Long userId, UserRequestDto userRequestDtoDetails) throws ResourceNotFoundException;

    void deleteUser(Long userId) throws ResourceNotFoundException;

    List<ContactResponseDto> saveUserContacts(Long userId, List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException;

    void deleteContactsByUserId(Long userId);

    List<ContactResponseDto> findContactsByUserId(Long userId) throws ResourceNotFoundException;

    List<ContactResponseDto> getCommonsContacts(Long userId1, Long userId2) throws ResourceNotFoundException;
}
