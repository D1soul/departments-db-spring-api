package ru.d1soul.model;

import ru.d1soul.web.service.MainDepartmentDeserializer;
import ru.d1soul.web.service.MainDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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

    @Column(name = "name")
    private String name;

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
