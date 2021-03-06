package ru.d1soul.departments.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.d1soul.departments.web.deserializer.RoleDeserializer;
import ru.d1soul.departments.web.serializer.RoleSerializer;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonSerialize(using = RoleSerializer.class)
@JsonDeserialize(using = RoleDeserializer.class)
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    @Column(name = "role")
    private String role;

    @NotNull
    @NonNull
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;
}
