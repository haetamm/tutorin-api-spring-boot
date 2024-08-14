package tutorin.com.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import tutorin.com.model.User;

public interface UserService extends UserDetailsService {
    User getByUserId(String id);
}
