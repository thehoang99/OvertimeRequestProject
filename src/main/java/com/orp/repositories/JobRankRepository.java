package com.orp.repositories;

import com.orp.model.JobRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRankRepository extends JpaRepository<JobRank, Integer> {
}
