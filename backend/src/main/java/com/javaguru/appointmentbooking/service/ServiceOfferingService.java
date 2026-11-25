package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.ServiceOfferingRequest;
import com.javaguru.appointmentbooking.dto.response.PagedResponse;
import com.javaguru.appointmentbooking.dto.response.ServiceOfferingResponse;
import com.javaguru.appointmentbooking.entity.ServiceOffering;
import com.javaguru.appointmentbooking.repository.ServiceOfferingRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import com.javaguru.appointmentbooking.utils.errorhandling.ServiceOfferingNotFoundException;
import com.javaguru.appointmentbooking.utils.mapper.ServiceOfferingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final ServiceOfferingMapper mapper;

    @Transactional
    public ServiceOfferingResponse createService(ServiceOfferingRequest request) {

        if (serviceOfferingRepository.existsByServiceCode(request.serviceCode())) {
            throw new BusinessRuleViolationException("Service code already exists.");
        }

        ServiceOffering service = mapper.toEntity(request);
        service.setActive(true);

        return mapper.toResponse(serviceOfferingRepository.save(service));
    }

    @Transactional
    public PagedResponse<ServiceOfferingResponse> getServices(Pageable pageable) {

        Page<ServiceOffering> serviceOfferingPage = serviceOfferingRepository.findAll(pageable);

        return new PagedResponse<>(
                serviceOfferingPage.map(mapper::toResponse)
                        .getContent(),
                serviceOfferingPage.getTotalElements(),
                serviceOfferingPage.getTotalPages(),
                serviceOfferingPage.getNumber(),
                serviceOfferingPage.getSize()
        );
    }

    @Transactional
    public void deactivateService(UUID serviceId) {

        ServiceOffering service = serviceOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceOfferingNotFoundException("Service not found."));

        service.setActive(false);
        serviceOfferingRepository.save(service);
    }

}
