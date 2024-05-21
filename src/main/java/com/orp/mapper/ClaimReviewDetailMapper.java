package com.orp.mapper;

import com.orp.dto.ClaimReviewDetailDTO;
import com.orp.model.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimReviewDetailMapper {
    @Mapping(source = "claimId", target = "id")
    @Mapping(source = "claimDate", target = "date")
    @Mapping(source = "staffId", target = "working.staffId")
    @Mapping(source = "joinedProjectDate", target = "working.startDate")
    @Mapping(source = "leftProjectDate", target = "working.endDate")
    @Mapping(source = "staffName", target = "working.staff.name")
    @Mapping(source = "departmentName", target = "working.staff.department.name")
    @Mapping(source = "projectName", target = "working.project.name")
    @Mapping(source = "projectStartDate", target = "working.project.startDate")
    @Mapping(source = "projectEndDate", target = "working.project.endDate")
    @Mapping(source = "jobRankName", target = "working.jobRank.name")
    Claim toEntity(ClaimReviewDetailDTO claimReviewDetailDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ClaimReviewDetailDTO toDto(Claim claim);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimReviewDetailDTO claimReviewDetailDTO, @MappingTarget Claim claim);
}