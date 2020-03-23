package ru.d1soul.departments.db.app.api.service;

import ru.d1soul.departments.db.app.model.MainDeptEmployee;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MainDeptEmployeesService {
    List<MainDeptEmployee> findAll(Sort sort);
    Optional<MainDeptEmployee> findByFullName(String lastName, String firstName, String middleName);
    MainDeptEmployee save(MainDeptEmployee mainDeptEmployee);
    void deleteByFullName(String lastName, String firstName, String middleName);
}
