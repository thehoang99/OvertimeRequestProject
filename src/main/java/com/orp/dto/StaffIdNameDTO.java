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
public class StaffIdNameDTO implements Serializable {
    private Integer id;
    private String name;
}