package com.departments_db_rest_api_angular.repository;

import com.departments_db_rest_api_angular.entities.MainDepartment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MainDepartmentRepository extends JpaRepository<MainDepartment, Long> {
    List<MainDepartment> findAll(Sort sort);
    Optional<MainDepartment> findByName(String name);
    void deleteByName(String name);
}
