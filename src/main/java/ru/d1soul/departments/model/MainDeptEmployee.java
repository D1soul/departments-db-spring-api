package ru.d1soul.departments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.d1soul.departments.web.deserializer.MainDepartmentDeserializer;
import ru.d1soul.departments.web.serializer.MainDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "main_dept_employees")
public class MainDeptEmployee implements Serializable {

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

    @JsonSerialize(using = MainDepartmentSerializer.class)
    @JsonDeserialize(using = MainDepartmentDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "md_id", nullable = false)
    private MainDepartment mainDepartment;

    public String toString(){
        return "\n Id: " + id + "\n  First Name: " + firstName
                + "\n  Middle Name: " + middleName + "\n Last Name: " + lastName
                + "\n Birth Date: " + birthDate + "\n Passport: " + passport
                + "\n MD Id" + mainDepartment.getId();
    }
}
