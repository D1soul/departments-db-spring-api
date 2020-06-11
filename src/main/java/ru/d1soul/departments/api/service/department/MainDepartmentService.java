package ru.d1soul.departments.api.service.department;

import ru.d1soul.departments.model.MainDepartment;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface MainDepartmentService {
    List<MainDepartment> findAll(Sort sort);
    MainDepartment findByName(String name);
    MainDepartment save(MainDepartment mainDepartment);
    MainDepartment update(String name, MainDepartment mainDepartment);
    void deleteByName(String name);
}
