package com.orp.controller;

import com.orp.model.Claim;
import com.orp.model.Staff;
import com.orp.model.Status;
import com.orp.model.Working;
import com.orp.services.ClaimService;
import com.orp.services.WorkingService;
import com.orp.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private WorkingService workingService;

    @GetMapping("/create")
    public String createUI(Model model) {
        Claim claim = new Claim();
        claim.setStatus(Status.DRAFT);

        createClaimExtract(model, claim);
        return "view/claim/create";
    }
    @PostMapping("/create")
    public String createDB(
            @ModelAttribute(name = "claim") Claim claim,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Claim claimDB = claimService.save(claim, result);
            if (claimDB == null) {
                isError = true;
            }
        }

        if (isError) {
            createClaimExtract(model, claim);
            model.addAttribute("errorMsg", "There are a few errors during claim creation!");
            return "view/claim/create";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Create a new Claim successfully!");
        return "redirect:/claim/myDraft";
    }

    @GetMapping("/workingDetail")
    public String showWorkingDetail(
            @RequestParam Integer workingId,
            Model model
    ) {
        Working working = workingService.detail(workingId);
        model.addAttribute("working", working);
        return "view/claim/workingDetail";
    }

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

    @GetMapping("/detail")
    public String showDetail(
            @RequestParam Integer id,
            Model model
    ){
        if (id != null) {
            Claim claim = claimService.detail(id);
            if (claim != null) {
                model.addAttribute("claim", claim);
            }
        }
        return "view/claim/detail";
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

    private void createClaimExtract(Model model, Claim claim) {
        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        List<Working> workings = workingService.findByStaffId(currentStaff.getId());

        model.addAttribute("claim", claim);
        model.addAttribute("workings", workings);
        model.addAttribute("currentStaff", currentStaff);
    }


}
