package ru.d1soul.departments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "main_departments")
public class MainDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 7, max = 60)
    @Pattern(regexp = "^(([А-я]+\\s?)+|([A-z]+\\s?)+)$")
    @Column(name = "name")
    private String name;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "mainDepartment", cascade=CascadeType.ALL)
    private Set<MainDeptEmployee> employees;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "mainDepartment", cascade=CascadeType.ALL)
    private Set<SubDepartment> subDepartments;

    public String toString(){
        return "\n Id: " + id + "\n Name: " + name;
    }
}

