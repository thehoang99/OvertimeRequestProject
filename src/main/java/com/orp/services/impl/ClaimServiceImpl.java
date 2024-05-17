package com.orp.services.impl;

import com.orp.model.Claim;
import com.orp.model.Status;
import com.orp.model.Working;
import com.orp.repositories.ClaimRepository;
import com.orp.repositories.WorkingRepository;
import com.orp.services.ClaimService;
import com.orp.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private WorkingRepository workingRepository;

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

    @Override
    public Claim detail(Integer claimId) {
        return claimRepository.findById(claimId).orElse(null);
    }

    @Override
    public Claim save(Claim claim, BindingResult result) {
        Working working = workingRepository.findById(claim.getWorkingId()).orElse(null);
        if (working == null) {
            return null;
        }

        Integer staffId = working.getStaffId();
        List<Status> statusList = List.of(Status.REJECTED, Status.CANCELLED);
        LocalDate claimDate = claim.getDate();
        LocalTime fromTime = claim.getFromTime();
        LocalTime toTime = claim.getToTime();

        List<Claim> claims = claimRepository.findClaimByStaffIdAndDateAndTime(staffId, statusList, claimDate, fromTime, toTime);
        if (!claims.isEmpty()) {
            result.rejectValue("fromTime", "Claim.Create.MSG5");
            return null;
        }

        addAuditTrailExtract("Created on: ", claim);
        return claimRepository.save(claim);
    }

    @Override
    public boolean cancel(Integer claimId, Integer staffId) {
        if (claimId == null || staffId == null) {
            return false;
        }

        Claim claim = claimRepository.findClaimByIdAndStaffId(claimId, staffId);

        if (claim == null) {
            return false;
        }

        Status status = claim.getStatus();
        if (status != Status.DRAFT && status != Status.PENDING) {
            return  false;
        }

        claim.setStatus(Status.CANCELLED);
        addAuditTrailExtract("Cancelled on: ", claim);
        claimRepository.save(claim);
        return true;
    }

    private void addAuditTrailExtract(String prefixMessage, Claim claim) {
        String newLine = prefixMessage + LocalDateTime.now() + " by " + CurrentUserUtils.getStaffInfo().getName();
        String currentAuditTrail = claim.getAuditTrail();

        if (currentAuditTrail == null) {
            claim.setAuditTrail(newLine);
        } else {
             String newAuditTrail = currentAuditTrail + "\n" + newLine;
             claim.setAuditTrail(newAuditTrail);
        }
    }

}
