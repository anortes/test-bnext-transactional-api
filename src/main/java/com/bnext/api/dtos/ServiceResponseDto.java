package com.bnext.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceResponseDto implements Serializable {

    private boolean valid;
    private String country;
    private String type;
    private String location;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("prefix-network")
    private String prefixNetwork;

    @JsonProperty("international-number")
    private String internationalNumber;

    @JsonProperty("local-number")
    private String localNumber;

    @JsonProperty("currency-code")
    private String currencyCode;

    @JsonProperty("international-calling-code")
    private Integer internationalCallingCode;

    @JsonProperty("is-mobile")
    private boolean isMobile;

    @JsonProperty("country-code3")
    private String currencyCode3;
}
