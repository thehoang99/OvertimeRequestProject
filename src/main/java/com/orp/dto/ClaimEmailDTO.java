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
public class ClaimEmailDTO implements Serializable {
    private Integer claimId;
    private Integer staffId;
    private String staffEmail;
    private String staffName;
    private String projectName;
    private LocalDate claimDate;
    private String remarks;
}