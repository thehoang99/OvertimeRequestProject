package com.orp.services;

import com.orp.model.Claim;
import com.orp.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClaimService {
    Page<Claim> findClaimByStaffIdAndStatus(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize);

    Page<Claim> findClaimByStaffIdAndStatusAndPM(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize);

    Page<Claim> findByStatus(Status status, Integer pageNumber, Integer pageSize);

    Claim detail(Integer claimId);

}
