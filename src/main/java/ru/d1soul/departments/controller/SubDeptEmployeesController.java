package ru.d1soul.departments.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.d1soul.departments.api.service.department.SubDeptEmployeesService;
import ru.d1soul.departments.model.SubDeptEmployee;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/departments-app")
public class SubDeptEmployeesController {

    private SubDeptEmployeesService subDeptEmployeesService;

    @Autowired
    public SubDeptEmployeesController(SubDeptEmployeesService subDeptEmployeesService) {
        this.subDeptEmployeesService = subDeptEmployeesService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/sub-dept_employees")
    public List<SubDeptEmployee> findAllSubDeptEmpls() {
        return subDeptEmployeesService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/sub-dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public SubDeptEmployee findSubDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName){
       return subDeptEmployeesService.findByFullName(lastName, firstName, middleName).orElseThrow(()-> {
           throw new NotFoundException("Сотрудник с Ф.И.О. : "
                   + lastName + " " + firstName + " " + middleName + " не обнаружен!");
       });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/sub-dept_employees")
    public SubDeptEmployee createSubDeptEmpl(@Valid @RequestBody SubDeptEmployee subDeptEmployee) {
        if (subDeptEmployeesService.findByFullName(subDeptEmployee.getLastName(),
                subDeptEmployee.getFirstName(), subDeptEmployee.getMiddleName()).isEmpty()) {
            return subDeptEmployeesService.save(subDeptEmployee);
        }
        else throw new BadFormException("Сотрудник с Ф.И.О. : "
                + subDeptEmployee.getLastName() + " "
                + subDeptEmployee.getFirstName()  + " "
                + subDeptEmployee.getMiddleName() + " уже существует");
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/sub-dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public SubDeptEmployee updateSubDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName,
            @Valid @RequestBody SubDeptEmployee updateSubEmpl){
        return subDeptEmployeesService.findByFullName(lastName, firstName, middleName).map(subEmpl -> {
            subEmpl.setFirstName(updateSubEmpl.getFirstName());
            subEmpl.setMiddleName(updateSubEmpl.getMiddleName());
            subEmpl.setLastName(updateSubEmpl.getLastName());
            subEmpl.setBirthDate(updateSubEmpl.getBirthDate());
            subEmpl.setPassport(updateSubEmpl.getPassport());
            subEmpl.setSubDepartment(updateSubEmpl.getSubDepartment());
            return subDeptEmployeesService.save(subEmpl);
        }).orElseThrow( ()-> {
            throw new NotFoundException("Сотрудник с Ф.И.О. : "
                    + lastName + " " + firstName + " " + middleName + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value =  "/sub-dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public void deleteSubDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName) {
        subDeptEmployeesService.deleteByFullName(lastName, firstName, middleName);
    }
}