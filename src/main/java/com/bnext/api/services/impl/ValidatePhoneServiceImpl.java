package com.bnext.api.services.impl;

import com.bnext.api.dtos.ServiceRequestDto;
import com.bnext.api.dtos.ServiceResponseDto;
import com.bnext.api.services.ValidatePhoneService;
import com.bnext.api.utils.CommonConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio que valida números de teléfono.
 */
@Service
public class ValidatePhoneServiceImpl implements ValidatePhoneService {

    /**
     * Servicio que valida un número de teléfono
     * @param serviceRequestDto Objeto con el formato de entrada que espera el servicio externo.
     *                          Contiene el número de teléfono que se va a enviar para su validación.
     * @return ServiceResponseDto Objeto con el formato de salida que devuelve el servicio externo.
     *         Contiene un valor boolean indicando la validez del número de teléfono,
     *         además de datos adicionales sobre el número de teléfono.
     */
    @Override
    public ServiceResponseDto validatePhone(ServiceRequestDto serviceRequestDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(CommonConstants.HEADER_USER_ID, CommonConstants.HEADER_USER_ID_VALUE);
        headers.add(CommonConstants.HEADER_API_KEY, CommonConstants.HEADER_API_KEY_VALUE);

        HttpEntity<ServiceRequestDto> request = new HttpEntity<>(serviceRequestDto, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ServiceResponseDto> response = restTemplate.postForEntity(
                CommonConstants.SERVICE_VALIDATE_PHONE_URL, request , ServiceResponseDto.class);
        return response.getBody();
    }
}