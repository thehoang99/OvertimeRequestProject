package com.orp.repositories;

import com.orp.dto.ProjectCreateWorkingDTO;
import com.orp.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p ORDER BY p.id DESC")
    Page<Project> findAll(Pageable pageable);

    @Query("SELECT new com.orp.dto.ProjectCreateWorkingDTO(p.id, p.name, p.startDate, p.endDate) FROM Project p")
    List<ProjectCreateWorkingDTO> findAllName();

}
