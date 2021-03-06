package ru.d1soul.departments.model;

import lombok.*;
import ru.d1soul.departments.web.deserializer.MainDepartmentDeserializer;
import ru.d1soul.departments.web.serializer.MainDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "sub_departments")
public class SubDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotNull
    @Pattern(regexp = "^(([А-яЁё]\\s?)+|([A-z]\\s?)+)$")
    @Column(name = "name")
    private String name;

    @NonNull
    @NotNull
    @JsonSerialize(using = MainDepartmentSerializer.class)
    @JsonDeserialize(using = MainDepartmentDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "md_id", nullable = false)
    private MainDepartment mainDepartment;

    @OrderBy("id asc ")
    @OneToMany(mappedBy = "subDepartment", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<SubDeptEmployee> employees;

    public String toString(){
        return "Id: " + id + "\n" + "Name: " + name + "\n"
             + "Main Dep id: " + mainDepartment.getId() + "\n";
    }
}
