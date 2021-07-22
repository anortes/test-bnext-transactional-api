package com.bnext.api.controllers.impl;

import com.bnext.api.controllers.UserController;
import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import com.bnext.api.services.UserService;
import com.bnext.api.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(CommonConstants.PATH_USERS)
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping()
    public ResponseEntity <List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.finAllUsers());
    }

    @PostMapping()
    public ResponseEntity <UserResponseDto>createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDto));
    }

    @GetMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity <UserResponseDto> getUserById(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userService.findByUserId(userId));
    }

    @PutMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity <UserResponseDto> updateUser(@PathVariable(value = CommonConstants.ID) Long userId,
                                                      @Valid @RequestBody UserRequestDto userRequestDto)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userService.saveUser(userId, userRequestDto));
    }

    @DeleteMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity deleteUser(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(CommonConstants.DELETED_USER_MESSAGE);
    }

    @PostMapping(CommonConstants.PATH_VARIABLE_ID + CommonConstants.PATH_CONTACTS)
    public ResponseEntity <List<ContactResponseDto>> saveContacts(@PathVariable(value = CommonConstants.ID) Long userId,
                                                                  @Valid @RequestBody List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUserContacts(userId, contactsRequestDto));
    }

    @GetMapping(CommonConstants.PATH_VARIABLE_ID + CommonConstants.PATH_CONTACTS)
    public ResponseEntity <List<ContactResponseDto>> findContacts(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.findContactsByUserId(userId));
    }

    @GetMapping(CommonConstants.PATH_CONTACTS)
    public ResponseEntity<List<ContactResponseDto>> getCommonContacts(
            @RequestParam Long userId1, @RequestParam Long userId2)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getCommonsContacts(userId1, userId2));
    }
}