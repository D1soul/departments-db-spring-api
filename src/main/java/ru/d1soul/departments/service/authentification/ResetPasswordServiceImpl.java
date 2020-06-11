package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.api.repository.authentification.ResetPasswordRepository;
import ru.d1soul.departments.api.service.authentification.ResetPasswordService;
import ru.d1soul.departments.model.PasswordResetToken;
import java.util.Optional;

@Service
@Transactional
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private  ResetPasswordRepository resetPasswordRepository;

    @Autowired
    public ResetPasswordServiceImpl(ResetPasswordRepository resetPasswordRepository) {
        this.resetPasswordRepository = resetPasswordRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return resetPasswordRepository.findByToken(token);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken resetToken) {
        return resetPasswordRepository.save(resetToken);
    }

    @Override
    public void deleteByToken(String token) {
        resetPasswordRepository.deleteByToken(token);
    }
}
