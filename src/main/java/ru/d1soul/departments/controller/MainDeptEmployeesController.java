package ru.d1soul.departments.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.d1soul.departments.model.MainDeptEmployee;
import ru.d1soul.departments.api.service.department.MainDeptEmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/main_dept_employees")
    public List<MainDeptEmployee> findAllMainDeptEmpl() {
        return mainDeptEmployeesService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public MainDeptEmployee findMainDeptEmplByFullName(
                           @PathVariable String lastName,
                           @PathVariable String firstName,
                           @PathVariable String middleName) {
       return mainDeptEmployeesService.findByFullName(lastName, firstName, middleName);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public MainDeptEmployee updateMainDeptEmplByFullName(
            @PathVariable  String lastName,
            @PathVariable  String firstName,
            @PathVariable  String middleName,
            @Valid @RequestBody MainDeptEmployee updateMainEmpl){
        return mainDeptEmployeesService.update(lastName, firstName, middleName, updateMainEmpl);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/main_dept_employees")
    public MainDeptEmployee createMainDeptEmpl(@Valid @RequestBody MainDeptEmployee mainDeptEmployee) {
      return mainDeptEmployeesService.save(mainDeptEmployee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/main_dept_employees/"
            + "{lastName}/{firstName}/{middleName}")
    public void deleteMainDeptEmplByFullName(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @PathVariable String middleName) {
        mainDeptEmployeesService.deleteByFullName(lastName, firstName, middleName);
    }
}