package com.orp.services;

import com.orp.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.BindingResult;

public interface StaffService {

    Staff save(Staff staff, BindingResult result);
}
