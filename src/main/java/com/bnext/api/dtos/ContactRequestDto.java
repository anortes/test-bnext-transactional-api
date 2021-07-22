package com.bnext.api.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactRequestDto {

    private long id;

    private long userId;

    @NotBlank
    @Size(max = 50)
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 15)
    private String phone;

}
