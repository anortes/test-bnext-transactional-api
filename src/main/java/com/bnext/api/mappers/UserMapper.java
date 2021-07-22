package com.bnext.api.mappers;

import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.repositories.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    UserResponseDto UserModelToUserResponseDto(UserModel userModel);
    UserModel UserResponseDtoToUserModel(UserResponseDto userResponseDto);
    UserModel UserRequestDtoToUserModel(UserRequestDto userRequestDto);
}
