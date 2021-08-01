package com.bnext.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {

    @NotBlank
    private ObjectId userId;

    @NotBlank
    @Size(max = 50)
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 15)
    private String phone;
}
