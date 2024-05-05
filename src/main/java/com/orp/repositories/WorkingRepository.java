package com.orp.repositories;

import com.orp.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingRepository extends JpaRepository<Working, Integer> {

    List<Working> findByStaffIdAndJobRankId(Integer staffId, Integer jobRankId);

}
