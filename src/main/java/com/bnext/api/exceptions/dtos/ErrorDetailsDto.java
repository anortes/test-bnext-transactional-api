package com.bnext.api.exceptions.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDetailsDto {

    private Date timestamp;
    private String message;
    private String details;

}