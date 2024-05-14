package com.orp.services;

import com.orp.model.Working;

import java.util.List;

public interface WorkingService {
    Boolean checkRecord(Integer staffId);

    List<Working> findByStaffId(Integer staffId);

    Working detail(Integer workingId);

}
