package com.departments_db_rest_api_angular.api.service;

import com.departments_db_rest_api_angular.entity.MainDepartment;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface MainDepartmentService {
    List<MainDepartment> findAll(Sort sort);
    Optional<MainDepartment> findByName(String name);
    MainDepartment save(MainDepartment mainDepartment);
    void deleteByName(String name);
}
