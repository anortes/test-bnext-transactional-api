package com.bnext.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceRequestDto implements Serializable {

    @NotBlank
    private String number;
    private String ip;

    @JsonProperty("country_code")
    private String countryCode;
}
