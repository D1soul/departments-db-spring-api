package com.departments_db_rest_api_angular.entity;

import com.departments_db_rest_api_angular.web_service.SubDepartmentDeserializer;
import com.departments_db_rest_api_angular.web_service.SubDepartmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "passport")
    private String passport;

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
