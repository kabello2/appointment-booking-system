package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(
            target = "branchId",
            expression =
                    "java(" +
                            "user.getBranch() != null ? " +
                            "user.getBranch().getId() : null" +
                            ")"
    )
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
