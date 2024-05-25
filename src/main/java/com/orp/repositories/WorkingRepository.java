package com.orp.repositories;

import com.orp.model.Staff;
import com.orp.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingRepository extends JpaRepository<Working, Integer> {

    List<Working> findByStaffIdAndJobRankId(Integer staffId, Integer jobRankId);

    List<Working> findByStaffId(Integer staffId);

    @Query("SELECT w.staff FROM Working w WHERE w.projectId = :projectId AND w.jobRankId = 1")
    Staff findPMByProjectId(Integer projectId);

   List<Working> findByProjectId(Integer projectId);

}
