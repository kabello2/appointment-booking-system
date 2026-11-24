package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.BranchRequest;
import com.javaguru.appointmentbooking.dto.response.BranchResponse;
import com.javaguru.appointmentbooking.entity.Branch;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BranchNotFoundException;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import com.javaguru.appointmentbooking.utils.mapper.BranchMapper;
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
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper mapper;

    public BranchResponse createBranch(BranchRequest request) {

        if (branchRepository.existsByBranchCode(request.branchCode())) {
            throw new BusinessRuleViolationException(
                    String.format("Branch code [%s] already exists.", request.branchCode()));
        }

        Branch branch = mapper.toEntity(request);
        return mapper.toResponse(branchRepository.save(branch));
    }

    public BranchResponse updateBranch(
            UUID branchId,
            BranchRequest request) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        branch.setName(request.name());
        branch.setAddressLine1(request.addressLine1());
        branch.setAddressLine2(request.addressLine2());
        branch.setSuburb(request.suburb());
        branch.setCity(request.city());
        branch.setProvince(request.province());
        branch.setPostalCode(request.postalCode());
        branch.setContactNumber(request.contactNumber());

        return mapper.toResponse(branchRepository.save(branch));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public BranchResponse getBranch(
            UUID branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        return mapper.toResponse(branch);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<BranchResponse> getBranches(Pageable pageable) {

        return branchRepository.findAll(pageable).map(mapper::toResponse);
    }

    public void deactivateBranch(
            UUID branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        branch.setActive(false);
        branchRepository.save(branch);
    }

}