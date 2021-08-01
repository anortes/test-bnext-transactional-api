package com.bnext.api.controllers.operations;

import com.bnext.api.ContactEntity;
import com.bnext.api.User;
import com.bnext.api.UserEntity;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

@Validated
public interface UserOperations<T extends User> {

    @Get()

    Single<List<T>> getAllUsers();

    @Post()
    Flowable<InsertOneResult> createUser(UserEntity userRequest);

    @Get("/{userId}")
    Single<T> getUserById(String userId);
            //throws ResourceNotFoundException;
    @Put("{userId}")
    @NonNull
    Flowable<UpdateResult> updateUser(String userId, UserEntity userRequest);
            //throws ResourceNotFoundException;

    @Post("{userId}/contacts")
    Flowable<InsertManyResult> saveContacts(@PathVariable String userId, @Body List<ContactEntity> contactsRequest);
            //throws ResourceNotFoundException, InvalidDataException;

    @Get("{userId}/contacts")
    Single<List<ContactEntity>>findContacts(String userId);
            //throws ResourceNotFoundException;

    @Get("/contacts")
    List<ContactEntity> getCommonContacts(@RequestAttribute String userId1, @RequestAttribute String userId2);
            //throws ResourceNotFoundException;
}