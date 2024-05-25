package com.orp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.orp.model.Project}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateWorkingDTO implements Serializable {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}