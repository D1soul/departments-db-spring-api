package ru.d1soul.departments.service.department;

import ru.d1soul.departments.api.repository.department.SubDeptEmployeesRepository;
import ru.d1soul.departments.api.service.department.SubDeptEmployeesService;
import ru.d1soul.departments.model.SubDeptEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import java.util.List;

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
        return subDeptEmployeesRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public SubDeptEmployee findByFullName(String lastName, String firstName, String middleName) {
        return subDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                lastName, firstName, middleName).orElseThrow(() -> {
            throw new NotFoundException("Сотрудник с Ф.И.О. : "
                    + lastName + " " + firstName + " " + middleName + " не обнаружен!");
        });
    }

    @Override
    public SubDeptEmployee save(SubDeptEmployee subDeptEmployee) {
        if (subDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                subDeptEmployee.getLastName(),
                subDeptEmployee.getFirstName(),
                subDeptEmployee.getMiddleName()).isEmpty()){
            return subDeptEmployeesRepository.save(subDeptEmployee);
        }
        else throw new BadFormException("Сотрудник с Ф.И.О. : "
                + subDeptEmployee.getLastName() + " "
                + subDeptEmployee.getFirstName()  + " "
                + subDeptEmployee.getMiddleName() + " уже существует");
    }

    @Override
    public SubDeptEmployee update(String lastName, String firstName,
                                   String middleName, SubDeptEmployee updSubDeptEmpl) {
        String uniqueLastName = updSubDeptEmpl.getLastName();
        String uniqueFirstName = updSubDeptEmpl.getFirstName();
        String uniqueMiddleName = updSubDeptEmpl.getMiddleName();
        return subDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                lastName, firstName, middleName).map(subDepEmpl -> {
            if (subDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleNameAndIdNot(
                    uniqueLastName, uniqueFirstName, uniqueMiddleName, subDepEmpl.getId()).isPresent()){
                throw new BadFormException("Сотрудник с Ф.И.О. : "
                        + updSubDeptEmpl.getLastName() + " "
                        + updSubDeptEmpl.getFirstName()  + " "
                        + updSubDeptEmpl.getMiddleName() + " уже существует");
            }
            else {
                subDepEmpl.setFirstName(updSubDeptEmpl.getFirstName());
                subDepEmpl.setMiddleName(updSubDeptEmpl.getMiddleName());
                subDepEmpl.setLastName(updSubDeptEmpl.getLastName());
                subDepEmpl.setBirthDate(updSubDeptEmpl.getBirthDate());
                subDepEmpl.setPassport(updSubDeptEmpl.getPassport());
                subDepEmpl.setSubDepartment(updSubDeptEmpl.getSubDepartment());
                return subDeptEmployeesRepository.save(subDepEmpl);
            }
        }).orElseThrow(() -> {
            throw new NotFoundException("Сотрудник с Ф.И.О. : "
                    + lastName + " " + firstName + " " + middleName + " не обнаружен!");
        });
    }

    @Override
    public void deleteByFullName(String lastName, String firstName, String middleName) {
        subDeptEmployeesRepository.deleteByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }
}
