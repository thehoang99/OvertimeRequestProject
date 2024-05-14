package com.orp.services.impl;

import com.orp.model.Claim;
import com.orp.model.Status;
import com.orp.repositories.ClaimRepository;
import com.orp.services.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Override
    public Page<Claim> findClaimByStaffIdAndStatus(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return claimRepository.findClaimByStaffIdAndStatus(staffId, statusList, pageable);
    }

    @Override
    public Page<Claim> findClaimByStaffIdAndStatusAndPM(Integer staffId, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return claimRepository.findClaimByStaffIdAndStatusAndPM(staffId, statusList, pageable);
    }

    @Override
    public Page<Claim> findByStatus(Status status, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return claimRepository.findByStatus(status, pageable);
    }
}
