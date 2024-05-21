package com.orp.dto;

import com.orp.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.orp.model.Claim}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimReviewDetailDTO implements Serializable {
    private Integer claimId;
    private LocalDate claimDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private Integer totalHours;
    private Status status;
    private Integer staffId;
    private LocalDate joinedProjectDate;
    private LocalDate leftProjectDate;
    private String staffName;
    private String departmentName;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String jobRankName;
    private String remarks;
}