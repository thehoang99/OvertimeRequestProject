package com.orp.mapper;

import com.orp.dto.ClaimEmailDTO;
import com.orp.model.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimEmailMapper {
    @Mapping(source = "claimId", target = "id")
    @Mapping(source = "staffId", target = "working.staffId")
    @Mapping(source = "staffEmail", target = "working.staff.email")
    @Mapping(source = "staffName", target = "working.staff.name")
    @Mapping(source = "projectName", target = "working.project.name")
    @Mapping(source = "claimDate", target = "date")
    Claim toEntity(ClaimEmailDTO claimEmailDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ClaimEmailDTO toDto(Claim claim);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimEmailDTO claimEmailDTO, @MappingTarget Claim claim);
}