package com.orp.controller;

import com.orp.dto.ProjectCreateWorkingDTO;
import com.orp.dto.StaffIdNameDTO;
import com.orp.model.JobRank;
import com.orp.model.Project;
import com.orp.model.Working;
import com.orp.services.JobRankService;
import com.orp.services.ProjectService;
import com.orp.services.StaffService;
import com.orp.services.WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/working")
public class WorkingController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private JobRankService jobRankService;
    @Autowired
    private WorkingService workingService;

    @GetMapping("/create")
    public String createWorkingUI(Model model) {
        Working working = new Working();
        createWorkingExtract(model, working);
        return "view/working/create";
    }

    @PostMapping("/create")
    public String createWorkingDB(
            @ModelAttribute(name = "working") Working working,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Working workingDB = workingService.save(working, result);
            if (workingDB == null) {
                isError = true;
            }
        }
        if (isError) {
            createWorkingExtract(model, working);
            model.addAttribute("errorMsg", "There are a few errors during working creation!");
            return "view/working/create";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Created the working successfully!");
        return "redirect:/working/view";
    }

    @GetMapping("/view")
    public String viewWorkingUI( Model model) {
        List<ProjectCreateWorkingDTO> projects = projectService.findAllName();
        model.addAttribute("projects", projects);
        return "view/working/view";
    }

    @GetMapping("/workingDetail")
    public String viewWorkingDetail(
            @RequestParam(name = "projectId", required = false) Integer projectId,
            Model model
    ) {
        List<Working> workings = workingService.findByProjectId(projectId);
        model.addAttribute("workings", workings);
        return "view/working/detail";
    }

    @GetMapping("/delete")
    public String deleteWorking(
            @RequestParam(name = "id", required = false) Integer workingId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (workingId != null) {
            boolean isDelete = workingService.deleteById(workingId);
            if (isDelete) {
                redirectAttributes.addFlashAttribute("successMsg", "Deleted the staff from the project successfully!");
                return "redirect:/working/view";
            }
        }
        model.addAttribute("errorMsg", "There are a few errors during working delete process!");
        return "view/working/view";
    }

    @GetMapping("/update")
    public String updateWorkingUI(
            @RequestParam(name = "id", required = false) Integer workingId,
            Model model
    ) {
        List<JobRank> jobRanks = jobRankService.findAll();
        model.addAttribute("jobRanks", jobRanks);

        if (workingId != null) {
            Working working = workingService.findById(workingId);
                if (working != null) {
                    model.addAttribute("working", working);
                    return "view/working/update";
                }
        }

        model.addAttribute("working", new Working());
        return "view/working/update";
    }

    @PostMapping("/update")
    public String updateWorkingDB(
            @ModelAttribute(name = "working") Working working,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Working workingDB = workingService.update(working);
            if (workingDB == null) {
                isError = true;
            }
        }

        if (isError) {
            model.addAttribute("working", new Working());
            model.addAttribute("errorMsg", "There are a few errors during working update process!");
            return "view/working/update";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Updated the working successfully!");
        return "redirect:/working/view";
    }

    private void createWorkingExtract(Model model, Working working) {
        List<ProjectCreateWorkingDTO> projects = projectService.findAllName();
        List<StaffIdNameDTO> staffs = staffService.findAllName();
        List<JobRank> jobRanks = jobRankService.findAll();

        model.addAttribute("working", working);
        model.addAttribute("projects", projects);
        model.addAttribute("staffs", staffs);
        model.addAttribute("jobRanks", jobRanks);
    }
}
