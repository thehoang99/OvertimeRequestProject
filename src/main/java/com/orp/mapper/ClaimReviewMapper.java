package com.orp.mapper;

import com.orp.dto.ClaimReviewDTO;
import com.orp.model.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimReviewMapper {
    Claim toEntity(ClaimReviewDTO claimReviewDTO);

    ClaimReviewDTO toDto(Claim claim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimReviewDTO claimReviewDTO, @MappingTarget Claim claim);
}