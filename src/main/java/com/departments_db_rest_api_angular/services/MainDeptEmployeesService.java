package com.departments_db_rest_api_angular.services;

import com.departments_db_rest_api_angular.entities.MainDeptEmployee;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface MainDeptEmployeesService {
    List<MainDeptEmployee> findAll(Sort sort);
    Optional<MainDeptEmployee> findByFullName(String lastName, String firstName, String middleName);
    MainDeptEmployee save(MainDeptEmployee mainDeptEmployee);
    void deleteByFullName(String lastName, String firstName, String middleName);
}