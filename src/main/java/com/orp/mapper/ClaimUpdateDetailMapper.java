package com.orp.mapper;

import com.orp.dto.ClaimUpdateDetailDTO;
import com.orp.model.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimUpdateDetailMapper {
    @Mapping(source = "projectName", target = "working.project.name")
    @Mapping(source = "jobRankName", target = "working.jobRank.name")
    @Mapping(source = "projectStartDate", target = "working.project.startDate")
    @Mapping(source = "projectEndDate", target = "working.project.endDate")
    @Mapping(source = "joinedProjectDate", target = "working.startDate")
    @Mapping(source = "leftProjectDate", target = "working.endDate")
    Claim toEntity(ClaimUpdateDetailDTO claimUpdateDetailDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ClaimUpdateDetailDTO toDto(Claim claim);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimUpdateDetailDTO claimUpdateDetailDTO, @MappingTarget Claim claim);
}