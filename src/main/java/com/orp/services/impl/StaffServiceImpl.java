package com.orp.services.impl;

import com.orp.dto.StaffDetailInfoDTO;
import com.orp.dto.StaffIdNameDTO;
import com.orp.model.Staff;
import com.orp.repositories.StaffRepository;
import com.orp.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

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

    @Override
    public List<StaffIdNameDTO> findAllName() {
        return staffRepository.findAllName();
    }

    @Override
    public StaffDetailInfoDTO findStaffDetailInfoById(Integer id) {
        return staffRepository.findStaffDetailInfoById(id);
    }

    @Override
    public Staff findById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff update(Staff staff) {
        if (staff == null) {
            return null;
        }

        Staff staffDB = staffRepository.findById(staff.getId()).orElse(null);
        if (staffDB == null) {
            return null;
        }

        staffDB.setDepartmentId(staff.getDepartmentId());
        staffDB.setRoleId(staff.getRoleId());
        staffDB.setSalary(staff.getSalary());

        return staffRepository.save(staffDB);
    }

}
