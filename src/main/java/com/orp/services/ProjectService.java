package com.orp.services;

import com.orp.model.Project;
import org.springframework.validation.BindingResult;

public interface ProjectService {
    Project save(Project project, BindingResult result);

}
