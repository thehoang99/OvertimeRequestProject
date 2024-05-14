package com.orp.controller;

import com.orp.model.Claim;
import com.orp.model.Staff;
import com.orp.model.Status;
import com.orp.services.ClaimService;
import com.orp.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @GetMapping("/myDraft")
    public String myDraft(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "My Draft";
        List<Status> statusList = List.of(Status.DRAFT);
        myClaimExtract(model, titleName, statusList, pageNumber, pageSize);

        return "view/claim/myClaim";
    }

    @GetMapping("/myPending")
    public String myPending(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "My pending approval";
        List<Status> statusList = List.of(Status.PENDING);
        myClaimExtract(model, titleName, statusList, pageNumber, pageSize);

        return "view/claim/myClaim";
    }

    @GetMapping("/myApproved")
    public String myApproved(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "My Approved";
        List<Status> statusList = List.of(Status.APPROVED);
        myClaimExtract(model, titleName, statusList, pageNumber, pageSize);

        return "view/claim/myClaim";
    }

    @GetMapping("/myPaid")
    public String myPaid(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "My Paid";
        List<Status> statusList = List.of(Status.PAID);
        myClaimExtract(model, titleName, statusList, pageNumber, pageSize);

        return "view/claim/myClaim";
    }

    @GetMapping("/myRejectOrCancel")
    public String myRejectOrCancel(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "My Rejected or Canceled";
        List<Status> statusList = List.of(Status.REJECTED, Status.CANCELLED);
        myClaimExtract(model, titleName, statusList, pageNumber, pageSize);

        return "view/claim/myClaim";
    }

    @GetMapping("/pendingApproval")
    public String pmPendingApproval(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "Claims for Pending Approval";
        List<Status> statusList = List.of(Status.PENDING);
        pmClaimExtract(model, titleName, statusList, pageNumber, pageSize);
        return  "view/claim/pm/pmClaim";
    }

    @GetMapping("/approvedOrPaid")
    public String pmApprovedOrPaid(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        String titleName = "Claims for Approved or Paid";
        List<Status> statusList = List.of(Status.APPROVED, Status.PAID);
        pmClaimExtract(model, titleName, statusList, pageNumber, pageSize);
        return  "view/claim/pm/pmClaim";
    }

    @GetMapping("/approved")
    public String financeApproved(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        if (currentStaff.getRoleId() == 2) {
            String titleName = "Claims for Waiting Pay";
            Page<Claim> claims = claimService.findByStatus(Status.APPROVED, pageNumber, pageSize);
            modelAddExtract(model, titleName, pageNumber, claims);
        }
        return "view/claim/finance/financeClaim";
    }

    @GetMapping("/paid")
    public String financePaid(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        if (currentStaff.getRoleId() == 2) {
            String titleName = "Claims for Paid";
            Page<Claim> claims = claimService.findByStatus(Status.PAID, pageNumber, pageSize);
            modelAddExtract(model, titleName, pageNumber, claims);
        }
        return  "view/claim/finance/financeClaim";
    }

    private void myClaimExtract(Model model, String titleName, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        Page<Claim> claims = claimService.findClaimByStaffIdAndStatus(staffId, statusList, pageNumber, pageSize);
        modelAddExtract(model, titleName, pageNumber, claims);
    }

    private void pmClaimExtract(Model model, String titleName, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        Page<Claim> claims = claimService.findClaimByStaffIdAndStatusAndPM(staffId, statusList, pageNumber, pageSize);
        modelAddExtract(model, titleName, pageNumber, claims);
    }

    private static void modelAddExtract(Model model, String titleName, Integer pageNumber, Page<Claim> claims) {
        model.addAttribute("claims", claims);
        model.addAttribute("titleName", titleName);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPage", claims.getTotalPages());
    }


}
