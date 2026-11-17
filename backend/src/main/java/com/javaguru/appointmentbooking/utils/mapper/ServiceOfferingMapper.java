package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.response.ServiceOfferingResponse;
import com.javaguru.appointmentbooking.entity.ServiceOffering;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfferingMapper {

    ServiceOfferingResponse toResponse(
            ServiceOffering serviceOffering
    );

    List<ServiceOfferingResponse> toResponseList(
            List<ServiceOffering> serviceOfferings
    );
}