package com.departments_db_rest_api_angular.api.service;

import com.departments_db_rest_api_angular.entity.SubDepartment;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface SubDepartmentService {
    List<SubDepartment> findAll(Sort sort);
    Optional<SubDepartment> findByName(String name);
    SubDepartment save(SubDepartment subDepartment);
    void deleteByName(String name);

}
