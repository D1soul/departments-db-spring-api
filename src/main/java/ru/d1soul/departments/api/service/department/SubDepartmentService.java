package ru.d1soul.departments.api.service.department;

import ru.d1soul.departments.model.SubDepartment;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface SubDepartmentService {
    List<SubDepartment> findAll(Sort sort);
    SubDepartment findByName(String name);
    SubDepartment save(SubDepartment subDepartment);
    SubDepartment update(String name, SubDepartment subDepartment);
    void deleteByName(String name);

}
