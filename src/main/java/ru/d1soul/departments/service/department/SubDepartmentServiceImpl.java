package ru.d1soul.departments.service.department;

import ru.d1soul.departments.api.service.department.SubDepartmentService;
import ru.d1soul.departments.model.SubDepartment;
import ru.d1soul.departments.api.repository.department.SubDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import java.util.List;

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
        return subDepartmentRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public SubDepartment findByName(String name) {
        return subDepartmentRepository.findByName(name).orElseThrow(()->{
            throw new NotFoundException("Подотдел с названием: " + name + " не обнаружен!");
        });
    }

     @Override
    public SubDepartment save(SubDepartment subDepartment) {
        if (subDepartmentRepository.findByName(subDepartment.getName()).isEmpty()){
            return subDepartmentRepository.save(subDepartment);
        }
        else throw new BadFormException("Подотдел с названием: "
                + subDepartment.getName() + " уже существует");
    }

    @Override
    public SubDepartment update(String name, SubDepartment updSubDept) {
        String uniqueName = updSubDept.getName();
        return subDepartmentRepository.findByName(name).map(subDep -> {
            if (subDepartmentRepository.findByNameAndIdNot(uniqueName, subDep.getId()).isPresent()){
                throw new BadFormException("Подотдел с названием: "
                                    + updSubDept.getName() + " уже существует");
            }
            else {
                subDep.setName(updSubDept.getName());
                subDep.setMainDepartment(updSubDept.getMainDepartment());
                return subDepartmentRepository.save(subDep);
            }
        }).orElseThrow(() -> {
            throw new NotFoundException("Подотдел с названием: " + name + " не обнаружен!");
        });
    }

    @Override
    public void deleteByName(String name) {
        subDepartmentRepository.deleteByName(name);
    }
}
