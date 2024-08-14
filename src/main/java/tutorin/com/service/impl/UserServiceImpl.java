package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tutorin.com.constant.StatusMessages;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.User;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User getByUserId(String id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.USER_NOT_FOUND));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
