package com.javaguru.appointmentbooking.utils.mapper;

import com.javaguru.appointmentbooking.dto.request.BranchRequest;
import com.javaguru.appointmentbooking.dto.response.BranchResponse;
import com.javaguru.appointmentbooking.entity.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface BranchMapper {

    BranchResponse toResponse(Branch branch);

    List<BranchResponse> toResponseList(List<Branch> branches);

    Branch toEntity(BranchRequest request);
}