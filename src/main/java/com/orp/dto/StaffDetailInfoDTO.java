package com.orp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.orp.model.Staff}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDetailInfoDTO implements Serializable {
    private Integer id;
    private String email;
    private String roleName;
    private String departmentName;
}