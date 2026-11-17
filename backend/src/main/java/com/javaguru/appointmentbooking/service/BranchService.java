package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.BranchRequest;
import com.javaguru.appointmentbooking.dto.response.BranchResponse;
import com.javaguru.appointmentbooking.entity.Branch;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BranchNotFoundException;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchResponse createBranch(BranchRequest request) {

        if (branchRepository.existsByBranchCode(request.branchCode())) {
            throw new BusinessRuleViolationException(
                    String.format("Branch code [%s] already exists.", request.branchCode()));
        }

        Branch branch = Branch.builder()
                .branchCode(request.branchCode())
                .name(request.name())
                .addressLine1(request.addressLine1())
                .addressLine2(request.addressLine2())
                .suburb(request.suburb())
                .city(request.city())
                .province(request.province())
                .postalCode(request.postalCode())
                .contactNumber(request.contactNumber())
                .active(true).build();

        Branch savedBranch = branchRepository.save(branch);
        return mapToResponse(savedBranch);
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

        return mapToResponse(branchRepository.save(branch));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public BranchResponse getBranch(
            UUID branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        return mapToResponse(branch);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<BranchResponse> getBranches() {

        return branchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deactivateBranch(
            UUID branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        branch.setActive(false);
        branchRepository.save(branch);
    }

    private BranchResponse mapToResponse(
            Branch branch) {

        return new BranchResponse(
                branch.getId(),
                branch.getBranchCode(),
                branch.getName(),
                branch.getAddressLine1(),
                branch.getAddressLine2(),
                branch.getSuburb(),
                branch.getCity(),
                branch.getProvince(),
                branch.getPostalCode(),
                branch.getContactNumber(),
                branch.getActive()
        );
    }
}