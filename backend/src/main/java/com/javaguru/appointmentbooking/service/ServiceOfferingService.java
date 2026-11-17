package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.ServiceOfferingRequest;
import com.javaguru.appointmentbooking.dto.response.ServiceOfferingResponse;
import com.javaguru.appointmentbooking.entity.ServiceOffering;
import com.javaguru.appointmentbooking.repository.ServiceOfferingRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import com.javaguru.appointmentbooking.utils.errorhandling.ServiceOfferingNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Transactional
    public ServiceOfferingResponse createService(ServiceOfferingRequest request) {

        if (serviceOfferingRepository.existsByServiceCode(request.serviceCode())) {
            throw new BusinessRuleViolationException("Service code already exists.");
        }

        ServiceOffering service = new ServiceOffering();
        service.setServiceCode(request.serviceCode());
        service.setName(request.name());
        service.setDescription(request.description());
        service.setDurationMinutes(request.durationMinutes());

        service.setActive(true);

        ServiceOffering saved =
                serviceOfferingRepository.save(
                        service);

        return mapToResponse(saved);
    }

    @Transactional
    public List<ServiceOfferingResponse>
    getServices() {

        return serviceOfferingRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public List<ServiceOfferingResponse>
    getActiveServices() {

        return serviceOfferingRepository
                .findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deactivateService(UUID serviceId) {

        ServiceOffering service = serviceOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceOfferingNotFoundException("Service not found."));

        service.setActive(false);
        serviceOfferingRepository.save(service);
    }

    private ServiceOfferingResponse mapToResponse(ServiceOffering serviceOffering) {

        return new ServiceOfferingResponse(
                serviceOffering.getId(),
                serviceOffering.getServiceCode(),
                serviceOffering.getName(),
                serviceOffering.getDescription(),
                serviceOffering.getDurationMinutes(),
                serviceOffering.getActive()
        );
    }
}
