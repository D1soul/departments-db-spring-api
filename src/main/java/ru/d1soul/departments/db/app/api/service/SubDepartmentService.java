package ru.d1soul.departments.db.app.api.service;

import ru.d1soul.departments.db.app.model.SubDepartment;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SubDepartmentService {
    List<SubDepartment> findAll(Sort sort);
    Optional<SubDepartment> findByName(String name);
    SubDepartment save(SubDepartment subDepartment);
    void deleteByName(String name);

}
