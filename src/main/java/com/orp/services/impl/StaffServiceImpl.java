package com.orp.services.impl;

import com.orp.model.Staff;
import com.orp.repositories.StaffRepository;
import com.orp.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Staff save(Staff staff, BindingResult result) {
        if (staff == null || result == null) {
            return null;
        }

        String email = staff.getEmail();
        if (staffRepository.existsByEmail(email)) {
            result.rejectValue("email", "Staff.Create.MSG1");
            return null;
        }

        String passwordEncode = passwordEncoder.encode(staff.getPassword());
        staff.setPassword(passwordEncode);

        return staffRepository.save(staff);
    }
}
