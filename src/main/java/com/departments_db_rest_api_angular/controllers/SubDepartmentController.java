package com.departments_db_rest_api_angular.controllers;

import com.departments_db_rest_api_angular.entities.SubDepartment;
import com.departments_db_rest_api_angular.services.SubDepartmentService;
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
public class SubDepartmentController {

    private SubDepartmentService subDepartmentService;

    @Autowired
    public SubDepartmentController(SubDepartmentService subDepartmentService) {
        this.subDepartmentService = subDepartmentService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sub-departments")
    public List<SubDepartment> findAllSubDepartments() {
        return subDepartmentService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sub-departments/{name}")
    public SubDepartment findSubDeptByName(@PathVariable String name) {
        Optional<SubDepartment> subDepartment = subDepartmentService.findByName(name);
        if (subDepartment.isPresent()) {
            return subDepartment.get();
        }
        else throw new NotFoundException("Sub-Department with Name: " + name + " Not Found!");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/sub-departments")
    public SubDepartment createSubDept(@Valid @RequestBody SubDepartment subDepartment) {
        return  subDepartmentService.save(subDepartment);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/sub-departments/{name}")
    public SubDepartment updateSubDeptByName(
            @PathVariable String name,
            @Valid @RequestBody SubDepartment updateSubDep){
        return subDepartmentService.findByName(name).map(subDep -> {
            subDep.setName(updateSubDep.getName());
            subDep.setMainDepartment(updateSubDep.getMainDepartment());
            return subDepartmentService.save(subDep);
        }).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/sub-departments/{name}")
    public void deleteSubDeptByName(@PathVariable String name) {
        subDepartmentService.deleteByName(name);
    }
}