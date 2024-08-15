package tutorin.com.service;

import tutorin.com.exception.NotFoundException;
import tutorin.com.model.User;

public interface UserService {
    User getByUserId(String id) throws NotFoundException;
}
