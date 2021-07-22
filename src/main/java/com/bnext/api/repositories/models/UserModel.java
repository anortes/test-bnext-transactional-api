package com.bnext.api.repositories.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "User")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserModel {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private long id;

    @NotBlank
    private String firstName;
    private String lastName;

    @NotBlank
    @Indexed(unique = true)
    private String email;

}
