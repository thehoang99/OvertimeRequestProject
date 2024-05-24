package com.orp.services.impl;

import com.orp.model.Project;
import com.orp.repositories.ProjectRepository;
import com.orp.services.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Project> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return projectRepository.findAll(pageable);
    }

    @Override
    public boolean cancel(Integer projectId) {
        if (projectRepository.existsById(projectId)) {
            projectRepository.deleteById(projectId);
            return true;
        }
        return false;
    }

    @Override
    public Project update(Project project, BindingResult result) {
        if (project == null || result == null) {
            return null;
        }

        Project projectDB = projectRepository.findById(project.getId()).orElse(null);
        if (projectDB == null) {
            return null;
        }

        BeanUtils.copyProperties(project, projectDB);
        return projectRepository.save(projectDB);
    }

    @Override
    public Project findById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

}
