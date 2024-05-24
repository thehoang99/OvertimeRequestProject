package com.orp.services;

import com.orp.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProjectService {
    Project save(Project project, BindingResult result);

    Page<Project> findAll(Integer pageNumber, Integer pageSize);

    boolean cancel(Integer projectId);

    Project update(Project project, BindingResult result);

    Project findById(Integer id);
}
