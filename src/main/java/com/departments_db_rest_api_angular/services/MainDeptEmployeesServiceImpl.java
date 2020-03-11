package com.departments_db_rest_api_angular.services;

import com.departments_db_rest_api_angular.entities.MainDeptEmployee;
import com.departments_db_rest_api_angular.repository.MainDeptEmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MainDeptEmployeesServiceImpl implements MainDeptEmployeesService {

    private MainDeptEmployeesRepository mainDeptEmployeesRepository;

    @Autowired
    public MainDeptEmployeesServiceImpl(MainDeptEmployeesRepository mainDeptEmployeesRepository){
        this.mainDeptEmployeesRepository = mainDeptEmployeesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainDeptEmployee> findAll(Sort sort) {
        return mainDeptEmployeesRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MainDeptEmployee> findByFullName(String lastName, String firstName, String middleName) {
        return mainDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(lastName, firstName, middleName);
    }

    @Override
    public MainDeptEmployee save(MainDeptEmployee mainDeptEmployee) {
        return mainDeptEmployeesRepository.save(mainDeptEmployee);
    }

    @Override
    public void deleteByFullName(String lastName, String firstName, String middleName) {
        mainDeptEmployeesRepository.deleteByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }
}
