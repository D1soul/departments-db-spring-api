package ru.d1soul.departments.api.repository.authentification;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d1soul.departments.model.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll(Sort sort);
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void deleteByUsername(String username);

    Optional<User> findByEmailAndIdNot(String email, Long id);

    Optional<User> findByUsernameAndIdNot(String username, Long id);
}

