package com.bnext.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
}
