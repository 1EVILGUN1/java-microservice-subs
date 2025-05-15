package com.microservice.subs.mapper;

import com.microservice.subs.dto.UserRequestDto;
import com.microservice.subs.dto.UserResponseDto;
import com.microservice.subs.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T21:07:14+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( dto.email() );
        user.setName( dto.name() );

        return user;
    }

    @Override
    public UserResponseDto toResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto.UserResponseDtoBuilder userResponseDto = UserResponseDto.builder();

        userResponseDto.id( user.getId() );
        userResponseDto.email( user.getEmail() );
        userResponseDto.name( user.getName() );
        userResponseDto.createdAt( user.getCreatedAt() );
        userResponseDto.updatedAt( user.getUpdatedAt() );

        return userResponseDto.build();
    }

    @Override
    public void updateEntityFromDto(UserRequestDto dto, User user) {
        if ( dto == null ) {
            return;
        }

        user.setEmail( dto.email() );
        user.setName( dto.name() );
    }
}
