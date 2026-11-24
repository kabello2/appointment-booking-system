package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.response.NotificationResponse;
import com.javaguru.appointmentbooking.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

//    NotificationResponse toResponse(
//            Notification notification
//    );
//
//    List<NotificationResponse> toResponseList(
//            List<Notification> notifications
//    );
}