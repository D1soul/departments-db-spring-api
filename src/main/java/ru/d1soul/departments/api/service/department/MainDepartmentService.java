package ru.d1soul.departments.api.service.department;

import ru.d1soul.departments.model.MainDepartment;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MainDepartmentService {
    List<MainDepartment> findAll(Sort sort);
    Optional<MainDepartment> findByName(String name);
    MainDepartment save(MainDepartment mainDepartment);
    void deleteByName(String name);
}
