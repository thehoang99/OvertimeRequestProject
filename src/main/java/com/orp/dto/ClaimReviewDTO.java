package com.orp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.orp.model.Claim}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimReviewDTO implements Serializable {
    private Integer id;
    private String remarks;

}