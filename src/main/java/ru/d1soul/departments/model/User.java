package ru.d1soul.departments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotNull
    @NonNull
    @Pattern(regexp = "^((\\d*[А-я]+\\d*)+|(\\d*[A-z]+\\d*)+)$")
    @Column(name = "username")
    private String username;

    @NotNull
    @NonNull
    @Email
    @Column(name = "email")
    private String email;

    @NotNull
    @NonNull
    @Column(name = "password")
    private String password;

    @NotNull
    @NonNull
    @Column(name = "confirm_password")
    private String confirmPassword;

    @NotNull
    @NonNull
    @JsonFormat(pattern="dd/MMMM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @NotNull
    @NonNull
    @Pattern(regexp = "((^male$)|(^female$))")
    @Column(name = "gender")
    private String gender;

    @NotNull
    @NonNull
    @Column(name = "is_banned")
    private Boolean isBanned;

    @NotNull
    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name="users_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id") )
    private Set<Role> roles;
}