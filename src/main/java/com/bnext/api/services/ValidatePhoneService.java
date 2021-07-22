package com.bnext.api.services;

import com.bnext.api.dtos.ServiceRequestDto;
import com.bnext.api.dtos.ServiceResponseDto;

public interface ValidatePhoneService {

    ServiceResponseDto validatePhone(ServiceRequestDto serviceRequestDto);
}