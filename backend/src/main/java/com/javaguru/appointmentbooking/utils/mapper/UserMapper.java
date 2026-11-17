package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(
            User user
    );

    List<UserResponse> toResponseList(
            List<User> users
    );
}
