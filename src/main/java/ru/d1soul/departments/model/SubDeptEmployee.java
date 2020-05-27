package ru.d1soul.departments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.d1soul.departments.web.deserializer.SubDepartmentDeserializer;
import ru.d1soul.departments.web.serializer.SubDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_dept_employees")
public class SubDeptEmployee implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^([А-яЁё]+|[A-z]+)$")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^([А-яЁё]+|[A-z]+)$")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^(([А-яЁё]+|[A-z]+)|(-))$")
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @JsonFormat(pattern="dd/MMMM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date")
    private Date birthDate;

    @NotNull
    @Pattern(regexp = "^(Серия:\\s?)\\d{2}\\s"
            + "\\d{2}\\s(Номер:\\s?)\\d{6}$")
    @Column(name = "passport")
    private String passport;

    @NotNull
    @JsonSerialize(using = SubDepartmentSerializer.class)
    @JsonDeserialize(using = SubDepartmentDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "sd_id", nullable = false)
    private SubDepartment subDepartment;

    public String toString(){
        return "Id: " + id + "\n" + "First Name: " + firstName + "\n"
                + "Middle Name: " + middleName + "\n" + "Last Name: " + lastName + "\n"
                + "Birth Date: " + birthDate + "\n" + "Passport: " + passport + "\n"
                + "SD Id" + subDepartment.getId() + "\n";
    }
}
