package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.PasswordResetToken;
import java.util.Optional;

public interface ResetPasswordService {
    Optional<PasswordResetToken> findByToken(String token);
    PasswordResetToken save(PasswordResetToken resetToken);
    void deleteByToken(String token);
}
