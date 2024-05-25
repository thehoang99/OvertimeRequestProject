package com.orp.services.impl;

import com.orp.model.JobRank;
import com.orp.repositories.JobRankRepository;
import com.orp.services.JobRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRankServiceImpl implements JobRankService {

    @Autowired
    private JobRankRepository jobRankRepository;

    @Override
    public List<JobRank> findAll() {
        return jobRankRepository.findAll();
    }
}
