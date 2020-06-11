package ru.d1soul.departments.service.department;

import ru.d1soul.departments.model.MainDepartment;
import ru.d1soul.departments.api.repository.department.MainDepartmentRepository;
import ru.d1soul.departments.api.service.department.MainDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import java.util.List;

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
        return mainDepartmentRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public MainDepartment findByName(String name) {
        return mainDepartmentRepository.findByName(name).orElseThrow(()->{
            throw new NotFoundException("Департамент с названием: " + name + " не обнаружен!");
        });
    }

    @Override
    public MainDepartment save(MainDepartment mainDepartment) {
        if (mainDepartmentRepository.findByName(mainDepartment.getName()).isEmpty()){
            return mainDepartmentRepository.save(mainDepartment);
        }
        else throw new BadFormException("Департамент с названием: "
                + mainDepartment.getName() + " уже существует");
    }

    @Override
    public MainDepartment update(String name, MainDepartment updMainDept) {
        String uniqueName = updMainDept.getName();
        return mainDepartmentRepository.findByName(name).map(mainDep -> {
            if (mainDepartmentRepository.findByNameAndIdNot(uniqueName, mainDep.getId()).isPresent()){
                throw new BadFormException("Департамент с названием: "
                        + updMainDept.getName() + " уже существует");
            }
            else {
                mainDep.setName(updMainDept.getName());
                return mainDepartmentRepository.save(mainDep);
            }
        }).orElseThrow(() -> {
            throw new NotFoundException("Департамент с названием: " + name + " не обнаружен!");
        });
    }

    @Override
    public void deleteByName(String name) {
        mainDepartmentRepository.deleteByName(name);
    }
}
