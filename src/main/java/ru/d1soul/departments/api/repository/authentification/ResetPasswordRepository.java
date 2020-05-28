package ru.d1soul.departments.api.repository.authentification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d1soul.departments.model.PasswordResetToken;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
}
