package com.bnext.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactResponseDto {

    private long id;
    private long userId;
    private String firstName;
    private String lastName;
    private String phone;
}
