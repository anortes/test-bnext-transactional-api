package com.bnext.api.controllers;

import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {

    ResponseEntity<List<UserResponseDto>> getAllUsers();

    ResponseEntity<UserResponseDto> createUser(UserRequestDto userRequestDto);

    ResponseEntity<UserResponseDto> getUserById(Long userId)
            throws ResourceNotFoundException;

    ResponseEntity<UserResponseDto> updateUser(Long userId, UserRequestDto userRequestDto)
            throws ResourceNotFoundException;

    ResponseEntity <List<ContactResponseDto>> saveContacts(Long userId, List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException;

    ResponseEntity <List<ContactResponseDto>> findContacts(Long userId)
            throws ResourceNotFoundException;

    ResponseEntity<List<ContactResponseDto>> getCommonContacts(Long userId1, Long userId2)
            throws ResourceNotFoundException;

}
