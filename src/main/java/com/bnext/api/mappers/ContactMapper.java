package com.bnext.api.mappers;

import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.repositories.models.ContactModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper( ContactMapper.class );
    ContactResponseDto ContactModelToContactResponseDto(ContactModel contactModel);
    ContactModel ContactRequestDtoToContactModel(ContactRequestDto contactRequestDto);
}
