package com.bnext.api.repositories.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "Contact")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactModel {

    @Transient
    public static final String SEQUENCE_NAME = "contacts_sequence";

    @Id
    private long id;

    @NotBlank
    private long userId;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    private String lastName;

    @NotBlank
    @Size(max = 15)
    private String phone;

}
