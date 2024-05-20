package com.orp.services;

import com.orp.dto.ClaimUpdateDTO;
import com.orp.model.Claim;
import com.orp.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ClaimService {
    Page<Claim> findClaimByStaffIdAndStatus(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize);

    Page<Claim> findClaimByStaffIdAndStatusAndPM(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize);

    Page<Claim> findByStatus(Status status, Integer pageNumber, Integer pageSize);

    Claim detail(Integer claimId);

    Claim save(Claim claim, BindingResult result);

    boolean cancel(Integer claimId, Integer staffId);

    boolean submit(Integer claimId, Integer staffId);

    Claim findClaimByIdAndStaffId(Integer claimId, Integer staffId);

    Claim update(ClaimUpdateDTO claim, BindingResult result);
}
