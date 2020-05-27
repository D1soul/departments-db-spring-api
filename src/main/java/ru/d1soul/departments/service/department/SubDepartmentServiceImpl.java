package ru.d1soul.departments.service.department;

import ru.d1soul.departments.api.service.department.SubDepartmentService;
import ru.d1soul.departments.model.SubDepartment;
import ru.d1soul.departments.api.repository.department.SubDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubDepartmentServiceImpl implements SubDepartmentService {

    private SubDepartmentRepository subDepartmentRepository;

    @Autowired
    public SubDepartmentServiceImpl(SubDepartmentRepository subDepartmentRepository) {
        this.subDepartmentRepository = subDepartmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubDepartment> findAll(Sort sort) {
        return subDepartmentRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubDepartment> findByName(String name) {
        return subDepartmentRepository.findByName(name);
    }

    @Override
    public SubDepartment save(SubDepartment subDepartment) {
        return subDepartmentRepository.save(subDepartment);
    }

    @Override
    public void deleteByName(String name) {
        subDepartmentRepository.deleteByName(name);
    }
}
