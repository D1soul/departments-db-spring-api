package ru.d1soul.departments.controller;

import ru.d1soul.departments.api.service.department.SubDepartmentService;
import ru.d1soul.departments.model.SubDepartment;
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
        return subDepartmentService.findByName(name).orElseThrow(()->{
           throw new NotFoundException("Подотдел с названием: " + name + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/sub-departments")
    public SubDepartment createSubDept(@Valid @RequestBody SubDepartment subDepartment) {
        if (subDepartmentService.findByName(subDepartment.getName()).isEmpty()) {
            return subDepartmentService.save(subDepartment);
        }
        else throw new BadFormException("Подотдел с названием: "
                + subDepartment.getName() + " уже существует");
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
        }).orElseThrow(()->{
            throw new NotFoundException("Подотдел с названием: " + name + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/sub-departments/{name}")
    public void deleteSubDeptByName(@PathVariable String name) {
        subDepartmentService.deleteByName(name);
    }
}