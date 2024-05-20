package com.orp.mapper;

import com.orp.dto.ClaimUpdateDTO;
import com.orp.model.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimUpdateMapper {
    Claim toEntity(ClaimUpdateDTO claimUpdateDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ClaimUpdateDTO toDto(Claim claim);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimUpdateDTO claimUpdateDTO, @MappingTarget Claim claim);
}