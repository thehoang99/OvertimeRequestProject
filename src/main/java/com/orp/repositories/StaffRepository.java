package com.orp.repositories;

import com.orp.dto.StaffDetailInfoDTO;
import com.orp.dto.StaffIdNameDTO;
import com.orp.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.orp.dto.StaffIdNameDTO(s.id, s.name) FROM Staff s ORDER BY s.id DESC")
    List<StaffIdNameDTO> findAllName();

    @Query("SELECT new com.orp.dto.StaffDetailInfoDTO(s.id, s.email, s.department.name, s.role.name) FROM Staff s WHERE s.id = :id")
    StaffDetailInfoDTO findStaffDetailInfoById(Integer id);

    List<Staff> findByRoleId(Integer roleId);

}
