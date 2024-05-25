package com.orp.services;

import com.orp.model.Working;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface WorkingService {
    Boolean checkRecord(Integer staffId);

    List<Working> findByStaffId(Integer staffId);

    Working detail(Integer workingId);

    Working save(Working working, BindingResult result);

    List<Working> findByProjectId(Integer projectId);

    boolean deleteById(Integer workingId);

    Working findById(Integer workingId);

    Working update(Working working);

}
