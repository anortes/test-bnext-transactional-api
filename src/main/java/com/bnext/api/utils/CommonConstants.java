package com.bnext.api.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommonConstants {

    public static final String DELETED_USER_MESSAGE = "User deleted OK";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found for this id :: ";
    public static final String INVALID_PHONE_MESSAGE = "Some phone number is not valid";
    public static final String HEADER_USER_ID = "user-id";
    public static final String HEADER_API_KEY = "api-key";
    public static final String HEADER_USER_ID_VALUE = "anortesb";
    public static final String HEADER_API_KEY_VALUE = "KvBxw3uC21dh78kq2ZqYvu56PHj667oBkcyqveEQsiGBxQpy";
    public static final String SERVICE_VALIDATE_PHONE_URL = "https://neutrinoapi.net/phone-validate";
    public static final String PATH_USERS = "/users";
    public static final String PATH_CONTACTS = "/contacts";
    public static final String PATH_VARIABLE_ID = "/{id}";
    public static final String ID = "id";
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
}
