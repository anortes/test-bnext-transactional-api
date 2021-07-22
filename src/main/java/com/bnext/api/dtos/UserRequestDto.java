package com.bnext.api.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto implements Serializable {

    private long id;

    @NotBlank
    @Size(max = 50)
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

}
