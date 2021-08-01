package com.bnext.api.controllers;

import com.bnext.api.ContactEntity;
import com.bnext.api.ContactsConfiguration;
import com.bnext.api.UserEntity;
import com.bnext.api.UsersConfiguration;
import com.bnext.api.controllers.operations.UserOperations;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Controller;
import io.micronaut.validation.Validated;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/users")
@Validated
public class UserController implements UserOperations<UserEntity> {

    private final UsersConfiguration usersConfiguration;
    private final ContactsConfiguration contactsConfiguration;
    private MongoClient mongoClient;

    public UserController(
            UsersConfiguration usersConfiguration,
            ContactsConfiguration contactsConfiguration,
            MongoClient mongoClient) {
        this.usersConfiguration = usersConfiguration;
        this.contactsConfiguration = contactsConfiguration;
        this.mongoClient = mongoClient;

    }

    @Override
    public Single<List<UserEntity>> getAllUsers() {
        return Flowable.fromPublisher(
                getUserCollection()
                        .find()
        ).toList();
    }

    @Override
    public Flowable<InsertOneResult> createUser(UserEntity userRequest) {
        return Flowable.fromPublisher(
                getUserCollection()
                        .insertOne(userRequest));
    }

    @Override
    public Single<UserEntity> getUserById(String userId) {
        return Flowable.fromPublisher(
                getUserCollection()
                        .find(Filters.eq("_id", new ObjectId(userId))))
                        .firstOrError();
    }

    @Override
    public @NonNull Flowable<UpdateResult> updateUser(String userId, UserEntity userRequest) {
        return Flowable.fromPublisher(
                getUserCollection()
                        .replaceOne(Filters.eq("_id", new ObjectId(userId)),
                                userRequest));

    }

    @Override
    public Flowable<InsertManyResult> saveContacts(String userId, List<ContactEntity> contactsRequest) {
        Flowable.fromPublisher(
                getContactCollection()
                        .deleteMany(Filters.eq("userId", new ObjectId(userId))));
        contactsRequest.stream().forEach(contact -> contact.setUserId(new ObjectId(userId)));
        return Flowable.fromPublisher(
                getContactCollection()
                .insertMany(contactsRequest));

    }

    @Override
    public Single<List<ContactEntity>> findContacts(String userId) {
        return Flowable.fromPublisher(
                getContactCollection()
                        .find(Filters.eq("userId", new ObjectId(userId))))
                .toList();
    }

    @Override
    public List<ContactEntity> getCommonContacts(String userId1, String userId2) {
        Single<List<ContactEntity>> contactsByUserId1 = findContacts(userId1);
        List<String> phonesByUserId2 = findContacts(userId2).blockingGet().stream()
                .map(ContactEntity::getPhone)
                .collect(Collectors.toList());

        return  contactsByUserId1.blockingGet().stream()
                .filter(c -> phonesByUserId2.contains(c.getPhone()))
                .collect(Collectors.toList());
    }

    private MongoCollection<UserEntity> getUserCollection() {
        return mongoClient
                .getDatabase(usersConfiguration.getDatabaseName())
                .getCollection(usersConfiguration.getCollectionName(), UserEntity.class);
    }

    private MongoCollection<ContactEntity> getContactCollection() {
        return mongoClient
                .getDatabase(contactsConfiguration.getDatabaseName())
                .getCollection(contactsConfiguration.getCollectionName(), ContactEntity.class);
    }
}
