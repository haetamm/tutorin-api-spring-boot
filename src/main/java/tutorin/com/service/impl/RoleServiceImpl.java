package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.model.Role;
import tutorin.com.repository.RoleRepository;
import tutorin.com.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role saveOrGet(UserRoleEnum userRoleEnum) {
        return roleRepository.findByRole(userRoleEnum)
                .orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(userRoleEnum).build()));
    }
}
