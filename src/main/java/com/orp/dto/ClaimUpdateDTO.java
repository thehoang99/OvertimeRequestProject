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
public class ClaimUpdateDTO implements Serializable {
    private Integer id;
    private Integer workingId;
    private LocalDate date;
    private LocalTime fromTime;
    private LocalTime toTime;
    private Integer totalHours;
    private Status status;
    private String remarks;
}