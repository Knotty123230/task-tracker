package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.RegistrationRequest;
import com.example.tasktracker.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User registrationDtoToUser(RegistrationRequest registrationRequest);
}
