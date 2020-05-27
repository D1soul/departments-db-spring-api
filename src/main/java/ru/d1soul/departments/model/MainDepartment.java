package ru.d1soul.departments.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "main_departments")
public class MainDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @NonNull
    @NotNull
    @Pattern(regexp = "^(([А-яЁё]\\s?)+|([A-z]\\s?)+)$")
    @Column(name = "name")
    private String name;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "mainDepartment", cascade=CascadeType.ALL, orphanRemoval=true)
    private Set<MainDeptEmployee> employees;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "mainDepartment", cascade=CascadeType.ALL, orphanRemoval=true)
    private Set<SubDepartment> subDepartments;

    public String toString(){
        return "\n Id: " + id + "\n Name: " + name;
    }
}

