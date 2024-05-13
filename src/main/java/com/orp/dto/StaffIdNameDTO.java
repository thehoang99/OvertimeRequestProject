package com.orp.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.orp.model.Staff}
 */
@Value
public class StaffIdNameDTO implements Serializable {
    Integer id;
    String name;
}