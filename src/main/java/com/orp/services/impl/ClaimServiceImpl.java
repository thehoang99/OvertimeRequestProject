package com.orp.services.impl;

import com.orp.dto.ClaimReviewDTO;
import com.orp.dto.ClaimUpdateDTO;
import com.orp.mapper.ClaimUpdateMapper;
import com.orp.model.Claim;
import com.orp.model.Staff;
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
    @Autowired
    private ClaimUpdateMapper claimUpdateMapper;

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
        if (claim == null || result == null) {
            return null;
        }

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
        Claim claim = getCancelAndSubmitClaim(claimId, staffId);
        if (claim == null) return false;

        Status status = claim.getStatus();
        if (status != Status.DRAFT && status != Status.PENDING) {
            return  false;
        }

        claim.setStatus(Status.CANCELLED);
        addAuditTrailExtract("Cancelled on: ", claim);
        claimRepository.save(claim);
        return true;
    }

    @Override
    public boolean submit(Integer claimId, Integer staffId) {
        Claim claim = getCancelAndSubmitClaim(claimId, staffId);
        if (claim == null) return false;

        Status status = claim.getStatus();
        if (status != Status.DRAFT) {
            return false;
        }

        if (claim.getWorking().getJobRankId() == 1) {
            claim.setStatus(Status.APPROVED);
        } else {
            claim.setStatus(Status.PENDING);
        }

        addAuditTrailExtract("Submitted on: ", claim);
        claimRepository.save(claim);

        return true;
    }

    @Override
    public Claim findClaimByIdAndStaffId(Integer claimId, Integer staffId) {
        return claimRepository.findClaimByIdAndStaffId(claimId, staffId);
    }

    @Override
    public Claim update(ClaimUpdateDTO claim, BindingResult result) {
        if (claim == null || result == null) {
            return null;
        }

        Claim claimDB = claimRepository.findById(claim.getId()).orElse(null);
        if (claimDB == null) {
            return null;
        }

        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        if (!claimDB.getWorking().getStaffId().equals(staffId)) {
            return null;
        }

        Integer claimId = claim.getId();
        List<Status> statusList = List.of(Status.REJECTED, Status.CANCELLED);
        LocalDate claimDate = claim.getDate();
        LocalTime fromTime = claim.getFromTime();
        LocalTime toTime = claim.getToTime();

        List<Claim> claims = claimRepository.findOtherClaimByIdStaffIdAndDateAndTime(claimId, staffId, statusList, claimDate, fromTime, toTime);
        if (!claims.isEmpty()) {
            result.rejectValue("fromTime", "Claim.Update.MSG1");
            return null;
        }
        claimUpdateMapper.partialUpdate(claim, claimDB);
        addAuditTrailExtract("Updated on: ", claimDB);
        return claimRepository.save(claimDB);
    }

    @Override
    public Claim review(Integer claimId, Status status, boolean isFinance) {
        if (claimId == null || status == null) {
            return null;
        }

        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        if (isFinance) {
            String role = currentStaff.getRole().getName();
            if (role.equals("ROLE_FINANCE")) {
                return claimRepository.findByIdAndStatus(claimId, status);
            }
        }
        return claimRepository.findClaimByIdAndStatusAndPM(claimId, status, currentStaff.getId());
    }

    @Override
    public boolean approveReturnReject(ClaimReviewDTO claimReviewDTO, Status statusAfter) {
        if (claimReviewDTO == null || statusAfter == null) {
            return false;
        }

        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        Claim claim = claimRepository.findClaimByIdAndStatusAndPM(claimReviewDTO.getId(), Status.PENDING, staffId);
        String prefixMessage;

        if (claim == null) {
            return false;
        }

        switch (statusAfter) {
            case APPROVED -> {
                prefixMessage = "Approved on: ";
            }
            case DRAFT -> {
                prefixMessage = "Returned on: ";

            }
            case REJECTED -> {
                prefixMessage = "Rejected on: ";

            }
            default -> prefixMessage = "Action is failed!";
        }

        claim.setStatus(statusAfter);
        claim.setRemarks(claimReviewDTO.getRemarks());
        addAuditTrailExtract(prefixMessage, claim);
        claimRepository.save(claim);
        return true;
    }

    @Override
    public boolean paidRejectByFinance(ClaimReviewDTO claimReviewDTO, Status statusAfter) {
        if (claimReviewDTO == null || statusAfter == null) {
            return false;
        }

        Staff staff = CurrentUserUtils.getStaffInfo();
        if (!staff.getRole().getName().equals("ROLE_FINANCE")) {
            return false;
        }

        Claim claim = claimRepository.findByIdAndStatus(claimReviewDTO.getId(), Status.APPROVED);
        if (claim == null) {
            return false;
        }

        String prefixMessage;
        if (statusAfter.equals(Status.PAID)) {
            prefixMessage = "Paid on: ";
        } else {
            prefixMessage = "Rejected on: ";
        }

        claim.setStatus(statusAfter);
        claim.setRemarks(claimReviewDTO.getRemarks());
        addAuditTrailExtract(prefixMessage, claim);
        claimRepository.save(claim);
        return true;
    }

    private Claim getCancelAndSubmitClaim(Integer claimId, Integer staffId) {
        if (claimId == null || staffId == null) {
            return null;
        }

        Claim claim = claimRepository.findClaimByIdAndStaffId(claimId, staffId);
        if (claim == null) {
            return null;
        }
        return claim;
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
