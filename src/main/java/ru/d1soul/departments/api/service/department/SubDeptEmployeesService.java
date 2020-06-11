package ru.d1soul.departments.api.service.department;

import ru.d1soul.departments.model.SubDeptEmployee;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface SubDeptEmployeesService {
    List<SubDeptEmployee> findAll(Sort sort);
    SubDeptEmployee findByFullName(String lastName, String firstName, String middleName);
    SubDeptEmployee save(SubDeptEmployee subDeptEmployee);
    SubDeptEmployee update(String lastName, String firstName, String middleName, SubDeptEmployee subDeptEmployee);
    void deleteByFullName(String lastName, String firstName, String middleName);
}
