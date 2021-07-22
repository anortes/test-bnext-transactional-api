package com.bnext.api.dtos;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDto implements Serializable {

    private long id;
    private String firstName;
    private String lastName;
    private String email;

}
