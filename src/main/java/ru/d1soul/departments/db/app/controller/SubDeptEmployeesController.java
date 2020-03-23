package ru.d1soul.departments.db.app.controller;

import ru.d1soul.departments.db.app.model.SubDeptEmployee;
import ru.d1soul.departments.db.app.api.service.SubDeptEmployeesService;
import ru.d1soul.departments.db.app.web_service.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    @GetMapping(value = "/sub-dept_employees")
    public List<SubDeptEmployee> findAllSubDeptEmpls() {
        return subDeptEmployeesService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sub-dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public SubDeptEmployee findSubDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName){
        Optional<SubDeptEmployee> subEmployee = subDeptEmployeesService.findByFullName(lastName, firstName, middleName);
        if (subEmployee.isPresent()) {
            return subEmployee.get();
        } else throw new NotFoundException("Employee with Full Name: "
                + lastName + " " + firstName + " " + middleName + " Not Found!");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/sub-dept_employees")
    public SubDeptEmployee createSubDeptEmpl(@Valid @RequestBody SubDeptEmployee subDeptEmployee) {
        return  subDeptEmployeesService.save(subDeptEmployee);
    }

    @ResponseStatus(HttpStatus.OK)
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
        }).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value =  "/sub-dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public void deleteSubDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName) {
        subDeptEmployeesService.deleteByFullName(lastName, firstName, middleName);
    }
}