package com.departments_db_rest_api_angular.repository;

import com.departments_db_rest_api_angular.entities.SubDepartment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubDepartmentRepository extends JpaRepository<SubDepartment, Long> {
    List<SubDepartment> findAll(Sort sort);
    Optional<SubDepartment> findByName(String name);
    void deleteByName(String name);

}
