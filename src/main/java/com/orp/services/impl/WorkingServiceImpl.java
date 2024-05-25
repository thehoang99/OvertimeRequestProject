package com.orp.services.impl;

import com.orp.model.Working;
import com.orp.repositories.WorkingRepository;
import com.orp.services.WorkingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class WorkingServiceImpl implements WorkingService {
    @Autowired
    WorkingRepository workingRepository;

    @Override
    public Boolean checkRecord(Integer staffId) {
        List<Working> workingList = workingRepository.findByStaffIdAndJobRankId(staffId, 1);
        if (!workingList.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public List<Working> findByStaffId(Integer staffId) {
        return workingRepository.findByStaffId(staffId);
    }

    @Override
    public Working detail(Integer workingId) {
        return workingRepository.findById(workingId).orElse(null);
    }

    @Override
    public Working save(Working working, BindingResult result) {
        if (working == null || result == null) {
            return null;
        }

        List<Working> workings = workingRepository.findAll();
        for (Working w : workings) {
            if (w.getStaffId().equals(working.getStaffId()) && w.getProjectId().equals(working.getProjectId())) {
                result.rejectValue("staffId", "Working.Create.MSG1");
                return null;
            }
        }

        return workingRepository.save(working);
    }

    @Override
    public List<Working> findByProjectId(Integer projectId) {
        return workingRepository.findByProjectId(projectId);
    }

    @Override
    public boolean deleteById(Integer workingId) {
        if (workingId == null) {
            return false;
        }

        if (workingRepository.existsById(workingId)) {
            workingRepository.deleteById(workingId);
            return true;
        }
        return false;
    }

    @Override
    public Working findById(Integer workingId) {
        return workingRepository.findById(workingId).orElse(null);
    }

    @Override
    public Working update(Working working) {
        if (working == null) {
            return null;
        }

        Working workingDB = workingRepository.findById(working.getId()).orElse(null);
        if (workingDB == null) {
            return null;
        }

        workingDB.setJobRankId(working.getJobRankId());
        workingDB.setStartDate(working.getStartDate());
        if (working.getEndDate() != null) {
            workingDB.setEndDate(working.getEndDate());
        }
        return workingRepository.save(workingDB);
    }
}
