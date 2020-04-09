package ru.d1soul.departments.service.department;

import ru.d1soul.departments.api.repository.department.SubDeptEmployeesRepository;
import ru.d1soul.departments.api.service.department.SubDeptEmployeesService;
import ru.d1soul.departments.model.SubDeptEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubDeptEmployeesServiceImpl implements SubDeptEmployeesService {

    private SubDeptEmployeesRepository subDeptEmployeesRepository;

    @Autowired
    public SubDeptEmployeesServiceImpl(SubDeptEmployeesRepository subDeptEmployeesRepository) {
        this.subDeptEmployeesRepository = subDeptEmployeesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubDeptEmployee> findAll(Sort sort) {
        return subDeptEmployeesRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubDeptEmployee> findByFullName(String lastName, String firstName, String middleName) {
        return subDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(lastName, firstName, middleName);
    }

    @Override
    public SubDeptEmployee save(SubDeptEmployee subDeptEmployee) {
        return subDeptEmployeesRepository.save(subDeptEmployee);
    }

    @Override
    public void deleteByFullName(String lastName, String firstName, String middleName) {
        subDeptEmployeesRepository.deleteByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }
}