package com.orp.controller;

import com.orp.model.Project;
import com.orp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
