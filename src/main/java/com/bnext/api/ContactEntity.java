package com.bnext.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class ContactEntity extends Contact {

    @BsonCreator
    @JsonCreator
    public ContactEntity(
            @JsonProperty("userId")
            @BsonProperty("userId") ObjectId userId,
            @JsonProperty("firstName")
            @BsonProperty("firstName") String firstName,
            @JsonProperty("lastName")
            @BsonProperty("lastName") String lastName,
            @JsonProperty("phone")
            @BsonProperty("phone") String phone) {
        super(userId, firstName, lastName, phone);
    }

}
