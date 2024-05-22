package com.orp.services.impl;

import com.orp.dto.ClaimReviewDTO;
import com.orp.dto.ClaimUpdateDTO;
import com.orp.mapper.ClaimEmailMapper;
import com.orp.mapper.ClaimUpdateMapper;
import com.orp.model.Claim;
import com.orp.model.Staff;
import com.orp.model.Status;
import com.orp.model.Working;
import com.orp.repositories.ClaimRepository;
import com.orp.repositories.StaffRepository;
import com.orp.repositories.WorkingRepository;
import com.orp.services.ClaimService;
import com.orp.utils.CurrentUserUtils;
import com.orp.utils.email.EmailService;
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
    private StaffRepository staffRepository;
    @Autowired
    private WorkingRepository workingRepository;
    @Autowired
    private ClaimUpdateMapper claimUpdateMapper;
    @Autowired
    private ClaimEmailMapper claimEmailMapper;
    @Autowired
    private EmailService emailService;

    public static final String APPROVED_CONTENT_TO_FINANCE = "is approved and waiting for pay.";
    public static final String RETURNED_CONTENT_TO_CLAIMER = "is returned.";
    public static final String REJECTED_CONTENT_TO_CLAIMER = "is rejected.";
    public static final String PENDING_CONTENT_TO_APPROVER = "is submitted and waiting for approval.";
    public static final String APPROVED_URL_TO_FINANCE = "/claim/finance/review?claimId=";
    public static final String RETURNED_URL_TO_CLAIMER = "/claim/myClaim/update?id=";
    public static final String REJECT_URL_TO_CLAIMER = "/claim/myRejectOrCancel";
    public static final String PENDING_URL_TO_APPROVER = "/claim/pm/review?claimId=";
    public static final String PAID_CONTENT_TO_CLAIMER = "is paid.";
    public static final String PAID_URL_TO_CLAIMER = "/claim/myPaid";

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
            sendEmailToFinanceTeam(claim, EmailService.EMAIL_TEMP, APPROVED_URL_TO_FINANCE + claim.getId(), APPROVED_CONTENT_TO_FINANCE);
        } else {
            claim.setStatus(Status.PENDING);
            sendEmailToPM(claim, EmailService.EMAIL_TEMP, PENDING_URL_TO_APPROVER + claim.getId(), PENDING_CONTENT_TO_APPROVER);
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
                sendEmailToFinanceTeam(claim, EmailService.EMAIL_TEMP, APPROVED_URL_TO_FINANCE + claim.getId(), APPROVED_CONTENT_TO_FINANCE);
            }
            case DRAFT -> {
                prefixMessage = "Returned on: ";
                sendEmailToClaimer(claim, EmailService.EMAIL_TEMP, RETURNED_URL_TO_CLAIMER + claim.getId(), RETURNED_CONTENT_TO_CLAIMER);
            }
            case REJECTED -> {
                prefixMessage = "Rejected on: ";
                sendEmailToClaimer(claim, EmailService.EMAIL_TEMP, REJECT_URL_TO_CLAIMER, REJECTED_CONTENT_TO_CLAIMER);
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
            sendEmailToClaimer(claim, EmailService.EMAIL_TEMP, PAID_URL_TO_CLAIMER, PAID_CONTENT_TO_CLAIMER);
        } else {
            prefixMessage = "Rejected on: ";
            sendEmailToClaimer(claim, EmailService.EMAIL_TEMP, REJECT_URL_TO_CLAIMER, REJECTED_CONTENT_TO_CLAIMER);
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

    private void sendEmailToPM(Claim claim, String template, String url, String content) {
        Integer claimId = claim.getId();
        Integer projectId = claimRepository.findProjectIdByClaimId(claimId);
        Staff pm = workingRepository.findPMByProjectId(projectId);
        if (pm != null) {
            List<String> toList = List.of(pm.getEmail());
            emailService.sendHtmlEmail(claimEmailMapper.toDto(claim), toList, pm.getName(), template, url, content);
        }
    }

    private void sendEmailToFinanceTeam(Claim claim, String template, String url, String content) {
        List<Staff> financeTeam = staffRepository.findByRoleId(2);
        if (!financeTeam.isEmpty()) {
            List<String> toList = financeTeam.stream().map(Staff::getEmail).toList();
            emailService.sendHtmlEmail(claimEmailMapper.toDto(claim), toList, "Finance Team", template, url, content);
        }
    }

    private void sendEmailToClaimer(Claim claim, String template, String url, String content) {
        Integer claimId = claim.getId();
        Staff staff = claimRepository.findStaffByClaimId(claimId);
        if (staff != null) {
            List<String> toList = List.of(staff.getEmail());
            emailService.sendHtmlEmail(claimEmailMapper.toDto(claim), toList, staff.getName(), template, url, content);
        }
    }

}
