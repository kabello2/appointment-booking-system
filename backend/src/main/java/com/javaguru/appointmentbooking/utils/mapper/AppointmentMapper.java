package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.response.AppointmentResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(
            target = "customerId",
            source = "customer.id"
    )
    @Mapping(
            target = "consultantId",
            source = "consultant.id"
    )
    @Mapping(
            target = "consultantName",
            expression =
                    "java(appointment.getConsultant().getFirstName() + \" \" + appointment.getConsultant().getLastName())"
    )
    @Mapping(
            target = "branchId",
            source = "branch.id"
    )
    @Mapping(
            target = "branchName",
            source = "branch.name"
    )
    @Mapping(
            target = "serviceId",
            source = "serviceOffering.id"
    )
    @Mapping(
            target = "serviceName",
            source = "serviceOffering.name"
    )
    AppointmentResponse toResponse(
            Appointment appointment
    );

    List<AppointmentResponse> toResponseList(
            List<Appointment> appointments
    );
}

