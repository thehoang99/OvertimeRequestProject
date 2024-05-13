package com.orp.services;

import com.orp.model.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
}
