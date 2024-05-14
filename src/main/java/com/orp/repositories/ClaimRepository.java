package com.orp.repositories;

import com.orp.model.Claim;
import com.orp.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    @Query("SELECT c FROM Claim c WHERE c.working.staffId = :staffId AND c.status IN :statusList ORDER BY c.id DESC")
    Page<Claim> findClaimByStaffIdAndStatus(Integer staffId, List<Status> statusList, Pageable pageable);

    @Query("""
        SELECT c
        FROM Claim c
        WHERE c.status IN :statusList
        AND c.working.projectId IN 
            (
                SELECT w.projectId 
                FROM Working w
                WHERE w.staffId = :staffId
                AND w.jobRankId = 1
            )
        ORDER BY c.id DESC 
    """)
    Page<Claim> findClaimByStaffIdAndStatusAndPM(Integer staffId, List<Status> statusList, Pageable pageable);

    @Query("SELECT c FROM Claim c WHERE c.status = :status ORDER BY c.id DESC")
    Page<Claim> findByStatus(Status status, Pageable pageable);

}
