package tutorin.com.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import tutorin.com.annotation.user.UniqueEmail;
import tutorin.com.annotation.user.UniqueUsername;
import tutorin.com.repository.UserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        // Initialization code if needed
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !userRepository.existsByUsername(email);
    }
}

