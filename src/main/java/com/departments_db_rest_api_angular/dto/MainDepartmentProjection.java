package com.departments_db_rest_api_angular.dto;


import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public interface MainDepartmentProjection {
    @Value("#{target.name}")
    String getName();

    @Value("#{target.employees}")
    Set<MainEmployeesProjection> getMainEmployees();

}
