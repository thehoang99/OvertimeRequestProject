package com.orp.services.impl;

import com.orp.model.Working;
import com.orp.repositories.WorkingRepository;
import com.orp.services.WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
