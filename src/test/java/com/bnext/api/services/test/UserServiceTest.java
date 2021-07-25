package com.bnext.api.services.test;

import com.bnext.api.dtos.*;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import com.bnext.api.mappers.ContactMapper;
import com.bnext.api.repositories.ContactRepository;
import com.bnext.api.repositories.UserRepository;
import com.bnext.api.repositories.models.ContactModel;
import com.bnext.api.repositories.models.UserModel;
import com.bnext.api.services.ValidatePhoneService;
import com.bnext.api.services.impl.SequenceGeneratorService;
import com.bnext.api.services.impl.UserServiceImpl;
import com.bnext.api.utils.CommonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class UserServiceTest {

    private static final List<UserModel> USER_MODELS_LIST = new ArrayList<>();
    private static final List<ContactModel> CONTACT_MODELS_LIST = new ArrayList<>();
    private static final UserRequestDto USER_REQUEST_DTO = new UserRequestDto();
    private static final ServiceResponseDto SERVICE_RESPONSE_DTO = new ServiceResponseDto();
    private static final List<ContactRequestDto> CONTACT_REQUEST_DTO_LIST = new ArrayList<>();
    private static final Long LONG_VALUE = 1L;
    private static final String PHONE_VALUE = "678123456";
    private static final String COMMON_PHONE = "678123457";

    @Mock
    UserRepository userRepository;

    @Mock
    ContactRepository contactRepository;

    @Mock
    ValidatePhoneService validatePhoneService;

    @Mock
    SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);

        USER_MODELS_LIST.clear();

        USER_MODELS_LIST.addAll(CommonUtils.initUsersMockData());

        Mockito.when(userRepository.findAll()).thenReturn(USER_MODELS_LIST);
        Mockito.when(userRepository.save(Mockito.any(UserModel.class))).thenReturn(USER_MODELS_LIST.get(0));
        Mockito.when(userRepository.findById(LONG_VALUE)).thenReturn(java.util.Optional.ofNullable(USER_MODELS_LIST.get(0)));

        CONTACT_MODELS_LIST.clear();
        CONTACT_MODELS_LIST.addAll(CommonUtils.initContactsMockData(USER_MODELS_LIST));

        Mockito.when(contactRepository.saveAll(Mockito.anyCollection())).thenReturn(CONTACT_MODELS_LIST);

        CONTACT_REQUEST_DTO_LIST.add(ContactMapper.INSTANCE.ContactModelToContactRequestDto(CONTACT_MODELS_LIST.get(0)));

        Mockito.when(contactRepository.findContactsByUser(LONG_VALUE))
                .thenReturn(CONTACT_MODELS_LIST.stream().filter(contact -> contact.getUserId() == LONG_VALUE)
                        .collect(Collectors.toList()));

    }

    @Test
    public void findAllUsersTest() {

        final List<UserResponseDto> userResponseDtos = userServiceImpl.findAllUsers();

        Assert.assertNotNull(userResponseDtos);

        Assert.assertEquals(3, userResponseDtos.size());

        Assert.assertEquals(userResponseDtos.get(1).getId(), 2L);
        Assert.assertEquals("User " + LONG_VALUE, userResponseDtos.get(0).getFirstName());
        Assert.assertEquals("Test " + LONG_VALUE, userResponseDtos.get(0).getLastName());
        Assert.assertEquals("user" + LONG_VALUE + "@gmail.com", userResponseDtos.get(0).getEmail());
    }

    @Test
    public void addUserTest() {

        final UserResponseDto userResponseDto = userServiceImpl.addUser(USER_REQUEST_DTO);

        final UserModel userModel = USER_MODELS_LIST.get(0);

        Assert.assertNotNull(userResponseDto);

        Assert.assertEquals(userResponseDto.getId(), userModel.getId());
        Assert.assertEquals("User " + userResponseDto.getId(), userModel.getFirstName());
        Assert.assertEquals("Test " + userResponseDto.getId(), userModel.getLastName());
        Assert.assertEquals("user" + userResponseDto.getId() + "@gmail.com", userModel.getEmail());
    }

    @Test
    public void findByUserIdTest() throws ResourceNotFoundException {

        final UserResponseDto userResponseDto = userServiceImpl.findByUserId(LONG_VALUE);

        final UserModel userModel = USER_MODELS_LIST.get(0);

        Assert.assertNotNull(userResponseDto);

        Assert.assertEquals(userResponseDto.getId(), userModel.getId());
        Assert.assertEquals("User " + userResponseDto.getId(), userModel.getFirstName());
        Assert.assertEquals("Test " + userResponseDto.getId(), userModel.getLastName());
        Assert.assertEquals("user" + userResponseDto.getId() + "@gmail.com", userModel.getEmail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByUserIdTestError() throws ResourceNotFoundException {
        userServiceImpl.findByUserId(4L);
    }

    @Test
    public void saveUserTest() throws ResourceNotFoundException {

        final UserResponseDto userResponseDto = userServiceImpl.saveUser(LONG_VALUE, USER_REQUEST_DTO);

        final UserModel userModel = USER_MODELS_LIST.get(0);

        Assert.assertNotNull(userResponseDto);

        Assert.assertEquals(userResponseDto.getId(), userModel.getId());
        Assert.assertEquals("User " + userResponseDto.getId(), userModel.getFirstName());
        Assert.assertEquals("Test " + userResponseDto.getId(), userModel.getLastName());
        Assert.assertEquals("user" + userResponseDto.getId() + "@gmail.com", userModel.getEmail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveUserTestError() throws ResourceNotFoundException {
        userServiceImpl.saveUser(4L, USER_REQUEST_DTO);
    }

    @Test
    public void deleteUserTest() throws ResourceNotFoundException {
        userServiceImpl.deleteUser(LONG_VALUE);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUserTestError() throws ResourceNotFoundException {
        userServiceImpl.deleteUser(4L);
    }

   @Test
    public void saveUserContactsTest() throws ResourceNotFoundException, InvalidDataException {

       SERVICE_RESPONSE_DTO.setValid(Boolean.TRUE);
       Mockito.when(validatePhoneService.validatePhone(Mockito.any(ServiceRequestDto.class))).thenReturn(SERVICE_RESPONSE_DTO);

        final List<ContactResponseDto> contactResponseDtos = userServiceImpl.saveUserContacts(LONG_VALUE, CONTACT_REQUEST_DTO_LIST);

       final ContactModel contactModel = CONTACT_MODELS_LIST.get(0);

        Assert.assertNotNull(contactResponseDtos);

        Assert.assertEquals(5, contactResponseDtos.size());

        Assert.assertEquals(contactResponseDtos.get(1).getId(), 2);
        Assert.assertEquals(contactResponseDtos.get(0).getUserId(), contactModel.getUserId());
        Assert.assertEquals("Contact " + LONG_VALUE, contactResponseDtos.get(0).getFirstName());
        Assert.assertEquals("Test " + LONG_VALUE, contactResponseDtos.get(0).getLastName());
        Assert.assertEquals(PHONE_VALUE, contactResponseDtos.get(0).getPhone());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveUserContactsTestErrorNotFound() throws ResourceNotFoundException, InvalidDataException {
        SERVICE_RESPONSE_DTO.setValid(Boolean.TRUE);
        Mockito.when(validatePhoneService.validatePhone(Mockito.any(ServiceRequestDto.class))).thenReturn(SERVICE_RESPONSE_DTO);

        userServiceImpl.saveUserContacts(4L, CONTACT_REQUEST_DTO_LIST);
    }

    @Test(expected = InvalidDataException.class)
    public void saveUserContactsTestErrorInvalidData() throws ResourceNotFoundException, InvalidDataException {

        SERVICE_RESPONSE_DTO.setValid(Boolean.FALSE);
        Mockito.when(validatePhoneService.validatePhone(Mockito.any(ServiceRequestDto.class))).thenReturn(SERVICE_RESPONSE_DTO);

        userServiceImpl.saveUserContacts(LONG_VALUE, CONTACT_REQUEST_DTO_LIST);
    }

    @Test
    public void deleteContactsByUserIdTest() {
        userServiceImpl.deleteContactsByUserId(LONG_VALUE);
    }

    @Test
    public void findContactsByUserIdTest() throws ResourceNotFoundException {
        final List<ContactResponseDto> contactResponseDtos = userServiceImpl.findContactsByUserId(LONG_VALUE);

        final ContactModel contactModel = CONTACT_MODELS_LIST.get(0);

        Assert.assertNotNull(contactResponseDtos);

        Assert.assertEquals(3, contactResponseDtos.size());

        Assert.assertEquals(contactResponseDtos.get(1).getId(), 2);
        Assert.assertEquals(contactResponseDtos.get(0).getUserId(), contactModel.getUserId());
        Assert.assertEquals("Contact " + LONG_VALUE, contactResponseDtos.get(0).getFirstName());
        Assert.assertEquals("Test " + LONG_VALUE, contactResponseDtos.get(0).getLastName());
        Assert.assertEquals(PHONE_VALUE, contactResponseDtos.get(0).getPhone());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findContactsByUserIdTestError() throws ResourceNotFoundException {
        userServiceImpl.findContactsByUserId(4L);
    }

    @Test
    public void getCommonsContactsTest() throws ResourceNotFoundException {
        Mockito.when(userRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(USER_MODELS_LIST.get(1)));
        Mockito.when(contactRepository.findContactsByUser(2L))
                .thenReturn(CONTACT_MODELS_LIST.stream().filter(contact -> contact.getUserId() == 2L)
                        .collect(Collectors.toList()));

        final List<ContactResponseDto> contactResponseDtos = userServiceImpl.getCommonsContacts(LONG_VALUE, 2L);

        Assert.assertNotNull(contactResponseDtos);

        Assert.assertEquals(1, contactResponseDtos.size());

        Assert.assertEquals(COMMON_PHONE, contactResponseDtos.get(0).getPhone());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getCommonsContactsTestError() throws ResourceNotFoundException {
        Mockito.when(userRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(USER_MODELS_LIST.get(1)));

        userServiceImpl.getCommonsContacts(LONG_VALUE, 4L);
    }
}