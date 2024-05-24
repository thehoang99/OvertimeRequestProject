package com.orp.controller;

import com.orp.dto.ClaimReviewDTO;
import com.orp.dto.ClaimReviewDetailDTO;
import com.orp.dto.ClaimUpdateDTO;
import com.orp.dto.ClaimUpdateDetailDTO;
import com.orp.mapper.ClaimReviewDetailMapper;
import com.orp.mapper.ClaimReviewMapper;
import com.orp.mapper.ClaimUpdateDetailMapper;
import com.orp.mapper.ClaimUpdateMapper;
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
    @Autowired
    private ClaimUpdateMapper claimUpdateMapper;
    @Autowired
    private ClaimUpdateDetailMapper claimUpdateDetailMapper;
    @Autowired
    private ClaimReviewMapper claimReviewMapper;
    @Autowired
    private ClaimReviewDetailMapper claimReviewDetailMapper;


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
            modelAddExtract(model, titleName, pageNumber, pageSize, claims);
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
            modelAddExtract(model, titleName, pageNumber, pageSize, claims);
        }
        return "view/claim/finance/financeClaim";
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

    @GetMapping("/myClaim/cancel")
    public String cancelClaim(
            @RequestParam(name = "id", required = false) Integer claimId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (claimId != null) {
            Integer staffId = CurrentUserUtils.getStaffInfo().getId();
            boolean isCancel = claimService.cancel(claimId, staffId);
            if (isCancel) {
                redirectAttributes.addFlashAttribute("successMsg", "Cancelled the claim successfully!");
                return "redirect:/claim/myDraft";
            }
        }

        model.addAttribute("errorMsg", "There are a few errors during claim cancel process!");
        return "view/claim/myClaim";
    }

    @GetMapping("/myClaim/submit")
    public String submitClaim(
            @RequestParam(name = "id", required = false) Integer claimId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (claimId != null) {
            Integer staffId = CurrentUserUtils.getStaffInfo().getId();
            boolean isSubmit = claimService.submit(claimId, staffId);
            if (isSubmit) {
                redirectAttributes.addFlashAttribute("successMsg", "Submitted the claim successfully!");
                return "redirect:/claim/myDraft";
            }
        }

        model.addAttribute("errorMsg", "There are a few errors during claim submit process!");
        return "view/claim/myClaim";
    }

    @GetMapping("/myClaim/update")
    public String updateClaimUI(
            @RequestParam(name = "id", required = false) Integer claimId,
            Model model
    ) {
        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        model.addAttribute("currentStaff", currentStaff);

        if (claimId != null) {
            Claim claim = claimService.findClaimByIdAndStaffId(claimId, currentStaff.getId());
            if (claim != null) {
                if (claim.getStatus().equals(Status.DRAFT)) {
                    ClaimUpdateDTO claimUpdateDTO = claimUpdateMapper.toDto(claim);
                    ClaimUpdateDetailDTO claimUpdateDetailDTO = claimUpdateDetailMapper.toDto(claim);

                    model.addAttribute("claimUpdateDTO", claimUpdateDTO);
                    model.addAttribute("claimUpdateDetailDTO", claimUpdateDetailDTO);
                    return "view/claim/update";
                }
            }
        }

        model.addAttribute("claimUpdateDTO", new ClaimUpdateDTO());
        model.addAttribute("claimUpdateDetailDTO", new ClaimUpdateDetailDTO());
        model.addAttribute("Cannot find the claim to update!");
        return "view/claim/update";
    }

    @PostMapping("/myClaim/update")
    public String updateClaimDB(
            @ModelAttribute(name = "claimUpdateDTO") ClaimUpdateDTO claim,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Claim claimDB = claimService.update(claim, result);
            if (claimDB == null) {
                isError = true;
            }
        }

        if (isError) {
            Staff currentStaff = CurrentUserUtils.getStaffInfo();
            Claim oldclaim = claimService.findClaimByIdAndStaffId(claim.getId(), currentStaff.getId());

            if (oldclaim != null) {
                ClaimUpdateDetailDTO claimUpdateDetailDTO = claimUpdateDetailMapper.toDto(oldclaim);
                model.addAttribute("claimUpdateDetailDTO", claimUpdateDetailDTO);
            } else {
                model.addAttribute("claimUpdateDetailDTO", new ClaimUpdateDetailDTO());
            }

            model.addAttribute("currentStaff", currentStaff);
            model.addAttribute("claimUpdateDTO", claim);
            model.addAttribute("errorMsg", "There are a few errors during claim update process!");
            return "view/claim/update";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Updated the claim successfully!");
        return "redirect:/claim/myDraft";
    }

    @GetMapping("/pm/review")
    public String pmReviewClaimUI(
            @RequestParam(name = "claimId", required = false) Integer claimId,
            Model model
    ) {
        if (claimId != null) {
            Claim claim = claimService.review(claimId, Status.PENDING, false);
            if (claim != null) {
                ClaimReviewDTO claimReviewDTO = claimReviewMapper.toDto(claim);
                ClaimReviewDetailDTO claimReviewDetailDTO = claimReviewDetailMapper.toDto(claim);

                model.addAttribute("claimReviewDTO", claimReviewDTO);
                model.addAttribute("claimReviewDetailDTO", claimReviewDetailDTO);
                return "view/claim/pm/pmReview";
            }
        }

        model.addAttribute("claimReviewDTO", new ClaimReviewDTO());
        model.addAttribute("claimReviewDetailDTO", new ClaimReviewDetailDTO());
        model.addAttribute("errorMsg", "Cannot find the claim!");
        return "view/claim/pm/pmReview";
    }

    @GetMapping("/finance/review")
    public String financeReviewClaimUI(
            @RequestParam(name = "claimId", required = false) Integer claimId,
            Model model
    ) {
        if (claimId != null) {
            Claim claim = claimService.review(claimId, Status.APPROVED, true);
            if (claim != null) {
                ClaimReviewDTO claimReviewDTO = claimReviewMapper.toDto(claim);
                ClaimReviewDetailDTO claimReviewDetailDTO = claimReviewDetailMapper.toDto(claim);

                model.addAttribute("claimReviewDTO", claimReviewDTO);
                model.addAttribute("claimReviewDetailDTO", claimReviewDetailDTO);
                return "view/claim/finance/financeReview";
            }
        }

        model.addAttribute("claimReviewDTO", new ClaimReviewDTO());
        model.addAttribute("claimReviewDetailDTO", new ClaimReviewDetailDTO());
        model.addAttribute("errorMsg", "Cannot find the claim!");
        return "view/claim/finance/financeReview";
    }

    @PostMapping("/pmReview/approve")
    public String approveClaim(
            @ModelAttribute(name = "claimReviewDTO") ClaimReviewDTO claimReviewDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return approveReturnRejectExtract(claimReviewDTO, model, redirectAttributes, Status.APPROVED, "Approved");
    }

    @PostMapping("/pmReview/return")
    public String returnClaim(
            @ModelAttribute(name = "claimReviewDTO") ClaimReviewDTO claimReviewDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return approveReturnRejectExtract(claimReviewDTO, model, redirectAttributes, Status.DRAFT, "Returned");
    }

    @PostMapping("/pmReview/reject")
    public String rejectClaimByPM(
            @ModelAttribute(name = "claimReviewDTO") ClaimReviewDTO claimReviewDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return approveReturnRejectExtract(claimReviewDTO, model, redirectAttributes, Status.REJECTED, "Rejected");
    }

    @PostMapping("/financeReview/paid")
    public String paidClaim(
            @ModelAttribute(name = "claimReviewDTO") ClaimReviewDTO claimReviewDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return paidRejectFinance(claimReviewDTO, model, redirectAttributes, Status.PAID,  "Paid");
    }

    @PostMapping("/financeReview/reject")
    public String rejectClaimbyFinance(
            @ModelAttribute(name = "claimReviewDTO") ClaimReviewDTO claimReviewDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return paidRejectFinance(claimReviewDTO, model, redirectAttributes, Status.REJECTED,  "Rejected");
    }

    private void myClaimExtract(Model model, String titleName, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        Page<Claim> claims = claimService.findClaimByStaffIdAndStatus(staffId, statusList, pageNumber, pageSize);
        modelAddExtract(model, titleName, pageNumber, pageSize, claims);
    }

    private void pmClaimExtract(Model model, String titleName, List<Status> statusList, Integer pageNumber, Integer pageSize) {
        Integer staffId = CurrentUserUtils.getStaffInfo().getId();
        Page<Claim> claims = claimService.findClaimByStaffIdAndStatusAndPM(staffId, statusList, pageNumber, pageSize);
        modelAddExtract(model, titleName, pageNumber, pageSize, claims);
    }

    private static void modelAddExtract(Model model, String titleName, Integer pageNumber, Integer pageSize, Page<Claim> claims) {
        model.addAttribute("claims", claims);
        model.addAttribute("titleName", titleName);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPage", claims.getTotalPages());
    }

    private void createClaimExtract(Model model, Claim claim) {
        Staff currentStaff = CurrentUserUtils.getStaffInfo();
        List<Working> workings = workingService.findByStaffId(currentStaff.getId());

        model.addAttribute("claim", claim);
        model.addAttribute("workings", workings);
        model.addAttribute("currentStaff", currentStaff);
    }

    private String approveReturnRejectExtract(ClaimReviewDTO claimReviewDTO, Model model, RedirectAttributes redirectAttributes, Status statusAfter, String action) {
        if (claimReviewDTO != null) {
            boolean isAction = claimService.approveReturnReject(claimReviewDTO, statusAfter);
            if (isAction) {
                redirectAttributes.addFlashAttribute("successMsg", action+" the claim successfully!");
                return "redirect:/claim/pendingApproval";
            }
        }
        model.addAttribute("errorMsg", "There are a few errors during claim approval process!");
        return "view/claim/pm/pmReview";
    }

    private String paidRejectFinance(ClaimReviewDTO claimReviewDTO, Model model, RedirectAttributes redirectAttributes, Status statusAfter, String action) {
        if (claimReviewDTO != null) {
            boolean isAction = claimService.paidRejectByFinance(claimReviewDTO, statusAfter);
            if (isAction) {
                redirectAttributes.addFlashAttribute("successMsg", action+" the claim successfully!");
                return "redirect:/claim/approved";
            }
        }
        model.addAttribute("errorMsg", "There are a few errors during claim paid process!");
        return "view/claim/finance/financeReview";
    }

}
