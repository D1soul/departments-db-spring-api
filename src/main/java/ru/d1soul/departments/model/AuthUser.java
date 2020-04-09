package ru.d1soul.departments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class AuthUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "confirm_password")
    private String confirmPassword;

    @JsonFormat(pattern="dd/MMMM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name="users_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id") )
    private Set<Role> roles;
}
