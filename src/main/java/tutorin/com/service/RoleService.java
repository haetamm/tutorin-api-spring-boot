package tutorin.com.service;

import tutorin.com.constant.UserRoleEnum;
import tutorin.com.model.Role;

public interface RoleService {
    Role saveOrGet(UserRoleEnum userRoleEnum);
}
