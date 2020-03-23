package ru.d1soul.api.service;

import ru.d1soul.model.SubDepartment;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SubDepartmentService {
    List<SubDepartment> findAll(Sort sort);
    Optional<SubDepartment> findByName(String name);
    SubDepartment save(SubDepartment subDepartment);
    void deleteByName(String name);

}
