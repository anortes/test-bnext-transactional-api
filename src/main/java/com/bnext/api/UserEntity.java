package com.bnext.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UserEntity extends User {

    @BsonCreator
    @JsonCreator
    public UserEntity(
            @JsonProperty("firstName")
            @BsonProperty("firstName") String firstName,
            @JsonProperty("lastName")
            @BsonProperty("lastName") String lastName,
            @JsonProperty("email")
            @BsonProperty("email") String email) {
        super(firstName, lastName, email);
    }

}
