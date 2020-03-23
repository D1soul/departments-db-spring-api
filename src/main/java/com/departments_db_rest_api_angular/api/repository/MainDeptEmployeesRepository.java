package com.departments_db_rest_api_angular.api.repository;

import com.departments_db_rest_api_angular.entity.MainDeptEmployee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MainDeptEmployeesRepository extends JpaRepository<MainDeptEmployee, Long> {
    List<MainDeptEmployee> findAll(Sort sort);
    Optional<MainDeptEmployee> findByLastNameAndFirstNameAndAndMiddleName(
            String lastName, String firstName, String middleName);
    void deleteByLastNameAndFirstNameAndMiddleName(
            String lastName, String firstName, String middleName);
}
