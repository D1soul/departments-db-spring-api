package com.departments_db_rest_api_angular.services;

import com.departments_db_rest_api_angular.entities.SubDeptEmployee;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface SubDeptEmployeesService {
    List<SubDeptEmployee> findAll(Sort sort);
    Optional<SubDeptEmployee> findByFullName(String lastName, String firstName, String middleName);
    SubDeptEmployee save(SubDeptEmployee subDeptEmployee);
    void deleteByFullName(String lastName, String firstName, String middleName);
}
