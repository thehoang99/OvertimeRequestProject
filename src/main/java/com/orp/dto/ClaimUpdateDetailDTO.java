package com.orp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.orp.model.Claim}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimUpdateDetailDTO implements Serializable {
    private Integer id;
    private String projectName;
    private String jobRankName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDate joinedProjectDate;
    private LocalDate leftProjectDate;
}