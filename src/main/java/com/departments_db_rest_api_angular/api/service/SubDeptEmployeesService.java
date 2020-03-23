package com.departments_db_rest_api_angular.api.service;

import com.departments_db_rest_api_angular.entity.SubDeptEmployee;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface SubDeptEmployeesService {
    List<SubDeptEmployee> findAll(Sort sort);
    Optional<SubDeptEmployee> findByFullName(String lastName, String firstName, String middleName);
    SubDeptEmployee save(SubDeptEmployee subDeptEmployee);
    void deleteByFullName(String lastName, String firstName, String middleName);
}
