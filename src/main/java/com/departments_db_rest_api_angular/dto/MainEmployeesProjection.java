package com.departments_db_rest_api_angular.dto;

import com.departments_db_rest_api_angular.entities.MainDepartment;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

public interface MainEmployeesProjection {
    @JsonProperty("Abdula")
    @Value("#{target.lastName} #{target.firstName}")
    String getFIO();

    ////@Value("#{target.}")
    //String getFirstName();
}
