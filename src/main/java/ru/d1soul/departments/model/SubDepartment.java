package ru.d1soul.departments.model;

import ru.d1soul.departments.web.service.MainDepartmentDeserializer;
import ru.d1soul.departments.web.service.MainDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "sub_departments")
public class SubDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min=5, max = 60)
    @Pattern(regexp = "^(([А-я]+\\s?)+|([A-z]+\\s?)+)$")
    @Column(name = "name")
    private String name;

    @NotNull
    @JsonSerialize(using = MainDepartmentSerializer.class)
    @JsonDeserialize(using = MainDepartmentDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "md_id", nullable = false)
    private MainDepartment mainDepartment;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "subDepartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    private Set<SubDeptEmployee> employees;

    public String toString(){
        return "Id: " + id + "\n" + "Name: " + name + "\n"
             + "Main Dep id: " + mainDepartment.getId() + "\n";
    }
}
