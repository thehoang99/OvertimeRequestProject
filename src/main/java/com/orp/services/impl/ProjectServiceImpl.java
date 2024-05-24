package com.orp.services.impl;

import com.orp.model.Project;
import com.orp.repositories.ProjectRepository;
import com.orp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project save(Project project, BindingResult result) {
        if (project == null || result == null) {
            return null;
        }

        List<Project> projects = projectRepository.findAll();
        for (Project projectDB : projects) {
            if (projectDB.getCode().equals(project.getCode())) {
                result.rejectValue("code", "Project.Create.MSG1");
                return null;
            }
        }

        return projectRepository.save(project);
    }
}
