package com.orp.controller;

import com.orp.model.Project;
import com.orp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/create")
    public String createProjectUI(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "view/project/create";
    }

    @PostMapping("/create")
    public String createProjectDB(
            @ModelAttribute(name = "project") Project project,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Project projectDB = projectService.save(project, result);
            if (projectDB == null) {
                isError = true;
            }
        }

        if (isError) {
            model.addAttribute("project", project);
            model.addAttribute("errorMsg", "There are a few errors during project creation!");
            return "view/project/create";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Created the project successfully!");
        return "redirect:/project/view";
    }

    @GetMapping("/view")
    public String viewProjectUI(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Page<Project> projects = projectService.findAll(pageNumber, pageSize);
        model.addAttribute("projects", projects);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPage", projects.getTotalPages());
        return "view/project/view";
    }

    @GetMapping("/cancel")
    public String cancelProject(
            @RequestParam(name = "id", required = false) Integer projectId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (projectId != null) {
            boolean isDelete = projectService.cancel(projectId);
            if (isDelete) {
                redirectAttributes.addFlashAttribute("successMsg", "Deleted the project successfully!");
                return "redirect:/project/view";
            }
        }

        model.addAttribute("errorMsg", "There are a few errors during project delete process!");
        return "view/project/view";
    }

    @GetMapping("/update")
    public String updateProjectUI(
            @RequestParam(name = "id", required = false) Integer projectId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (projectId != null) {
            Project project = projectService.findById(projectId);
            if (project != null) {
                model.addAttribute("project", project);
                return "view/project/update";
            }
        }
        model.addAttribute("project", new Project());
        return "view/project/update";
    }

    @PostMapping("/update")
    public String updateProjectDB(
            @ModelAttribute(name = "project") Project project,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Project projectDB = projectService.update(project, result);
            if (projectDB == null) {
                isError = true;
            }
        }

        if (isError) {
            Project oldProject = projectService.findById(project.getId());
            if (oldProject != null) {
                model.addAttribute("project", oldProject);
            } else {
                model.addAttribute("project", new Project());
            }
            model.addAttribute("errorMsg", "There are a few errors during project update process!");
            return "view/project/update";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Updated the project successfully!");
        return "redirect:/project/view";
    }


}
