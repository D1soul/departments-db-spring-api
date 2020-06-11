package ru.d1soul.departments.service.department;

import ru.d1soul.departments.api.repository.department.MainDeptEmployeesRepository;
import ru.d1soul.departments.model.MainDeptEmployee;
import ru.d1soul.departments.api.service.department.MainDeptEmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import java.util.List;

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
        return mainDeptEmployeesRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public MainDeptEmployee findByFullName(String lastName, String firstName, String middleName) {
        return mainDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                                                   lastName, firstName, middleName).orElseThrow(() -> {
            throw new NotFoundException("Сотрудник с Ф.И.О. : "
                    + lastName + " " + firstName + " " + middleName + " не обнаружен!");
        });
    }

    @Override
    public MainDeptEmployee save(MainDeptEmployee mainDeptEmployee) {
        if (mainDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                mainDeptEmployee.getLastName(),
                mainDeptEmployee.getFirstName(),
                mainDeptEmployee.getMiddleName()).isEmpty()){
            return mainDeptEmployeesRepository.save(mainDeptEmployee);
        }
        else throw new BadFormException("Сотрудник с Ф.И.О. : "
                + mainDeptEmployee.getLastName() + " "
                + mainDeptEmployee.getFirstName()  + " "
                + mainDeptEmployee.getMiddleName() + " уже существует");
    }

    @Override
    public MainDeptEmployee update(String lastName, String firstName,
                                   String middleName, MainDeptEmployee updMainDeptEmpl) {
        String uniqueLastName = updMainDeptEmpl.getLastName();
        String uniqueFirstName = updMainDeptEmpl.getFirstName();
        String uniqueMiddleName = updMainDeptEmpl.getMiddleName();
        return mainDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleName(
                                                lastName, firstName, middleName).map(mainDepEmpl -> {
            if (mainDeptEmployeesRepository.findByLastNameAndFirstNameAndAndMiddleNameAndIdNot(
                    uniqueLastName, uniqueFirstName, uniqueMiddleName, mainDepEmpl.getId()).isPresent()){
                throw new BadFormException("Сотрудник с Ф.И.О. : "
                        + updMainDeptEmpl.getLastName() + " "
                        + updMainDeptEmpl.getFirstName()  + " "
                        + updMainDeptEmpl.getMiddleName() + " уже существует");
            }
            else {
                mainDepEmpl.setFirstName(updMainDeptEmpl.getFirstName());
                mainDepEmpl.setMiddleName(updMainDeptEmpl.getMiddleName());
                mainDepEmpl.setLastName(updMainDeptEmpl.getLastName());
                mainDepEmpl.setBirthDate(updMainDeptEmpl.getBirthDate());
                mainDepEmpl.setPassport(updMainDeptEmpl.getPassport());
                mainDepEmpl.setMainDepartment(updMainDeptEmpl.getMainDepartment());
                return mainDeptEmployeesRepository.save(mainDepEmpl);
            }
        }).orElseThrow(() -> {
            throw new NotFoundException("Сотрудник с Ф.И.О. : "
                    + lastName + " " + firstName + " " + middleName + " не обнаружен!");
        });
    }

    @Override
    public void deleteByFullName(String lastName, String firstName, String middleName) {
        mainDeptEmployeesRepository.deleteByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }
}
