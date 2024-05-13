package com.orp.services;

import com.orp.dto.StaffDetailInfoDTO;
import com.orp.dto.StaffIdNameDTO;
import com.orp.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface StaffService {

    Staff save(Staff staff, BindingResult result);

    List<StaffIdNameDTO> findAllName();

    StaffDetailInfoDTO findStaffDetailInfoById(Integer id);

    Staff findById(Integer id);

    Staff update(Staff staff);

}
