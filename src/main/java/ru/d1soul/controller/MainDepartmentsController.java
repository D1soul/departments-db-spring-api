package ru.d1soul.controller;

import ru.d1soul.api.service.MainDepartmentService;
import ru.d1soul.model.MainDepartment;
import ru.d1soul.web.service.NotFoundException;
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
public class MainDepartmentsController {

    private MainDepartmentService mainDepartmentService;

    @Autowired
    public MainDepartmentsController(MainDepartmentService mainDepartmentService) {
        this.mainDepartmentService = mainDepartmentService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/main_departments")
    public List<MainDepartment> findAllMainDepartments() {
        return mainDepartmentService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/main_departments/{name}")
    public MainDepartment findMainDeptByName(@PathVariable String name) {
        Optional<MainDepartment> mainDepartments = mainDepartmentService.findByName(name);
         if(mainDepartments.isPresent()) {
             return mainDepartments.get();
         }
         else throw new NotFoundException("Main Department with Name: " + name + " Not Found!");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/main_departments")
    public MainDepartment createMainDept(@Valid @RequestBody MainDepartment mainDepartment) {
        return  mainDepartmentService.save(mainDepartment);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/main_departments/{name}")
    public MainDepartment updateMainDeptByName(
            @PathVariable String name,
            @Valid @RequestBody MainDepartment mainDepUpdate) {
        return mainDepartmentService.findByName(name).map(mainDep -> {
            mainDep.setName(mainDepUpdate.getName());
            return mainDepartmentService.save(mainDep);
        }).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/main_departments/{name}")
    public void deleteMainDeptByName(@PathVariable String name) {
        mainDepartmentService.deleteByName(name);
    }
}

