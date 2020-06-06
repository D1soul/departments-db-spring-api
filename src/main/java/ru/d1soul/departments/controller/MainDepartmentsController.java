package ru.d1soul.departments.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.d1soul.departments.api.service.department.MainDepartmentService;
import ru.d1soul.departments.model.MainDepartment;
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
public class MainDepartmentsController {

    private MainDepartmentService mainDepartmentService;

    @Autowired
    public MainDepartmentsController(MainDepartmentService mainDepartmentService) {
        this.mainDepartmentService = mainDepartmentService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/main_departments")
    public List<MainDepartment> findAllMainDepartments() {
        return mainDepartmentService.findAll(Sort.by("id").ascending());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/main_departments/{name}")
    public MainDepartment findMainDeptByName(@PathVariable String name) {
        return mainDepartmentService.findByName(name).orElseThrow(()->{
            throw new NotFoundException("Департамент с названием: " + name + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/main_departments")
    public MainDepartment createMainDept(@Valid @RequestBody MainDepartment mainDepartment) {
        if (mainDepartmentService.findByName(mainDepartment.getName()).isEmpty()) {
            return mainDepartmentService.save(mainDepartment);
        }
        else throw new BadFormException("Департамент с названием: "
                                    + mainDepartment.getName() + " уже существует");
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/main_departments/{name}")
    public MainDepartment updateMainDeptByName(
            @PathVariable String name,
            @Valid @RequestBody MainDepartment mainDepUpdate) {
        return mainDepartmentService.findByName(name).map(mainDep -> {
            mainDep.setName(mainDepUpdate.getName());
                return mainDepartmentService.save(mainDep);
        }).orElseThrow( ()-> {
            throw new NotFoundException("Департамент с названием: " + name + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/main_departments/{name}")
    public void deleteMainDeptByName(@PathVariable String name) {
        mainDepartmentService.deleteByName(name);
    }
}

