package ru.d1soul.departments.service.department;

import ru.d1soul.departments.model.MainDepartment;
import ru.d1soul.departments.api.repository.department.MainDepartmentRepository;
import ru.d1soul.departments.api.service.department.MainDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MainDepartmentServiceImpl implements MainDepartmentService {

    private MainDepartmentRepository mainDepartmentRepository;

    @Autowired
    public MainDepartmentServiceImpl(MainDepartmentRepository mainDepartmentRepository) {
        this.mainDepartmentRepository = mainDepartmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainDepartment> findAll(Sort sort) {
        return mainDepartmentRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MainDepartment> findByName(String name) {
        return mainDepartmentRepository.findByName(name);
    }

    @Override
    public MainDepartment save(MainDepartment mainDepartment) {
        return mainDepartmentRepository.save(mainDepartment);
    }

    @Override
    public void deleteByName(String name) {
        mainDepartmentRepository.deleteByName(name);
    }
}
