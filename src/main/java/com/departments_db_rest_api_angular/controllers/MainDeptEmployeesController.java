package com.departments_db_rest_api_angular.controllers;

import com.departments_db_rest_api_angular.entities.MainDeptEmployee;
import com.departments_db_rest_api_angular.services.MainDeptEmployeesService;
import com.departments_db_rest_api_angular.web_services.NotFoundException;
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
public class MainDeptEmployeesController {

    private MainDeptEmployeesService mainDeptEmployeesService;

    @Autowired
    public MainDeptEmployeesController(MainDeptEmployeesService mainDeptEmployeesService) {
        this.mainDeptEmployeesService = mainDeptEmployeesService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/main_dept_employees")
    public List<MainDeptEmployee> findAllMainDeptEmpl() {
        return mainDeptEmployeesService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public MainDeptEmployee findMainDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName) {
        Optional<MainDeptEmployee> mainEmployee = mainDeptEmployeesService.findByFullName(lastName, firstName, middleName);
        if (mainEmployee.isPresent()) {
            return mainEmployee.get();
        }
        else throw new NotFoundException("Main Employee with Full Name: "
                + lastName + " " + firstName + " " + middleName + " Not Found!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public MainDeptEmployee updateMainDeptEmplByFullName(
            @PathVariable  String lastName,
            @PathVariable  String firstName,
            @PathVariable  String middleName,
            @Valid @RequestBody MainDeptEmployee updateMainEmpl){
        return mainDeptEmployeesService.findByFullName(lastName,firstName, middleName).map(mainDeptEmployee -> {
            mainDeptEmployee.setFirstName(updateMainEmpl.getFirstName());
            mainDeptEmployee.setMiddleName(updateMainEmpl.getMiddleName());
            mainDeptEmployee.setLastName(updateMainEmpl.getLastName());
            mainDeptEmployee.setBirthDate(updateMainEmpl.getBirthDate());
            mainDeptEmployee.setPassport(updateMainEmpl.getPassport());
            mainDeptEmployee.setMainDepartment(updateMainEmpl.getMainDepartment());
            return mainDeptEmployeesService.save(mainDeptEmployee);
        }).get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/main_dept_employees")
    public MainDeptEmployee createMainDeptEmpl(@Valid @RequestBody MainDeptEmployee mainDeptEmployee) {
        return  mainDeptEmployeesService.save(mainDeptEmployee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public void deleteMainDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName) {
        mainDeptEmployeesService.deleteByFullName(lastName, firstName, middleName);
    }
}

