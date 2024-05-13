package com.orp.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.orp.model.Staff}
 */
@Value
public class StaffDetailInfoDTO implements Serializable {
    Integer id;
    String email;
    String roleName;
    String departmentName;
}