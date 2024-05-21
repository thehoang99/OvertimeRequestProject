package com.orp.repositories;

import com.orp.model.Claim;
import com.orp.model.Staff;
import com.orp.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Query("""
        SELECT c
        FROM Claim c
        WHERE c.working.staffId = :staffId
        AND c.status NOT IN :statusList
        AND c.date = :claimDate
        AND ((c.fromTime BETWEEN :fromTime AND :toTime) OR (c.toTime BETWEEN :fromTime AND :toTime) OR (c.fromTime < :fromTime AND c.toTime > :toTime))
    """)
    List<Claim> findClaimByStaffIdAndDateAndTime(Integer staffId, List<Status> statusList, LocalDate claimDate, LocalTime fromTime, LocalTime  toTime);

    @Query("""
        SELECT c
        FROM Claim c
        WHERE c.id != :claimId
        AND c.working.staffId = :staffId
        AND c.status NOT IN :statusList
        AND c.date = :claimDate
        AND ((c.fromTime BETWEEN :fromTime AND :toTime) OR (c.toTime BETWEEN :fromTime AND :toTime) OR (c.fromTime < :fromTime AND c.toTime > :toTime))
    """)
    List<Claim> findOtherClaimByIdStaffIdAndDateAndTime(Integer claimId, Integer staffId, List<Status> statusList, LocalDate claimDate, LocalTime fromTime, LocalTime  toTime);

    @Query("SELECT c FROM Claim c WHERE c.id = :claimId AND c.working.staffId = :staffId")
    Claim findClaimByIdAndStaffId(Integer claimId, Integer staffId);

    Claim findByIdAndStatus(Integer claimId, Status status);

    @Query("""
        SELECT c
        FROM Claim c
        WHERE c.id = :claimId
        AND c.status = :status
        AND c.working.projectId IN
            (
                SELECT w.projectId
                FROM Working w
                WHERE w.staffId = :staffId
                AND w.jobRankId = 1
            )
    """)
    Claim findClaimByIdAndStatusAndPM(Integer claimId, Status status, Integer staffId);

    @Query("SELECT c.working.projectId FROM Claim c WHERE c.id = :id")
    Integer findProjectIdByClaimId(Integer id);

    @Query("SELECT c.working.staff FROM Claim c WHERE c.id = :id")
    Staff findStaffByClaimId(Integer id);
}
