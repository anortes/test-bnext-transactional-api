package com.bnext.api.controllers.test;

import com.bnext.api.controllers.impl.UserControllerImpl;
import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import com.bnext.api.services.impl.UserServiceImpl;
import com.bnext.api.utils.CommonConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserControllerTest {

    private static final UserResponseDto USER_RESPONSE_DTO = new UserResponseDto();
    private static final List<UserResponseDto> USER_RESPONSE_DTO_LIST = new ArrayList<>();
    private static final UserRequestDto USER_REQUEST_DTO = new UserRequestDto();
    private static final List<ContactRequestDto> CONTACT_REQUEST_DTO_LIST = new ArrayList<>();
    private static final List<ContactResponseDto> CONTACT_RESPONSE_DTO_LIST = new ArrayList<>();
    private static final Long LONG_VALUE = 1L;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserControllerImpl userControllerImp;

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsersTest() {

        Mockito.when(userServiceImpl.findAllUsers()).thenReturn(USER_RESPONSE_DTO_LIST);

        final ResponseEntity<List<UserResponseDto>> responseEntity = userControllerImp.getAllUsers();

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_OK, responseEntity.getStatusCodeValue());

        Assert.assertEquals(USER_RESPONSE_DTO_LIST, responseEntity.getBody());
    }

    @Test
    public void createUserTest() {

        Mockito.when(userServiceImpl.addUser(USER_REQUEST_DTO)).thenReturn(USER_RESPONSE_DTO);

        final ResponseEntity<UserResponseDto> responseEntity = userControllerImp.createUser(USER_REQUEST_DTO);

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_CREATED, responseEntity.getStatusCodeValue());

        Assert.assertEquals(USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void getUserByIdTest() throws ResourceNotFoundException {

        Mockito.when(userServiceImpl.findByUserId(Mockito.anyLong())).thenReturn(USER_RESPONSE_DTO);

        final ResponseEntity<UserResponseDto> responseEntity = userControllerImp.getUserById(Mockito.anyLong());

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_OK, responseEntity.getStatusCodeValue());

        Assert.assertEquals(USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void updateUserTest() throws ResourceNotFoundException {

        Mockito.when(userServiceImpl.saveUser(LONG_VALUE, USER_REQUEST_DTO)).thenReturn(USER_RESPONSE_DTO);

        final ResponseEntity<UserResponseDto> responseEntity = userControllerImp.updateUser(
                LONG_VALUE, USER_REQUEST_DTO);

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_OK, responseEntity.getStatusCodeValue());

        Assert.assertEquals(USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void saveContactsTest() throws ResourceNotFoundException, InvalidDataException {

        Mockito.when(userServiceImpl.saveUserContacts(LONG_VALUE, CONTACT_REQUEST_DTO_LIST))
                .thenReturn(CONTACT_RESPONSE_DTO_LIST);

        final ResponseEntity<List<ContactResponseDto>> responseEntity = userControllerImp.saveContacts(
                LONG_VALUE, CONTACT_REQUEST_DTO_LIST);

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_CREATED, responseEntity.getStatusCodeValue());

        Assert.assertEquals(CONTACT_RESPONSE_DTO_LIST, responseEntity.getBody());
    }

    @Test
    public void findContactsTest() throws ResourceNotFoundException {

        Mockito.when(userServiceImpl.findContactsByUserId(LONG_VALUE))
                .thenReturn(CONTACT_RESPONSE_DTO_LIST);

        final ResponseEntity<List<ContactResponseDto>> responseEntity = userControllerImp.findContacts(
                Mockito.anyLong());

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_OK, responseEntity.getStatusCodeValue());

        Assert.assertEquals(CONTACT_RESPONSE_DTO_LIST, responseEntity.getBody());
    }

    @Test
    public void getCommonContactsTest() throws ResourceNotFoundException {

        Mockito.when(userServiceImpl.getCommonsContacts(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(CONTACT_RESPONSE_DTO_LIST);

        final ResponseEntity<List<ContactResponseDto>> responseEntity = userControllerImp.getCommonContacts(
                Mockito.anyLong(), Mockito.anyLong());

        Assert.assertNotNull(responseEntity);

        Assert.assertEquals(CommonConstants.HTTP_STATUS_OK, responseEntity.getStatusCodeValue());

        Assert.assertEquals(CONTACT_RESPONSE_DTO_LIST, responseEntity.getBody());
    }
}
