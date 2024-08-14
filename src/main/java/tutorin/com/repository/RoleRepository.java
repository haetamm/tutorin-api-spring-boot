package tutorin.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRoleEnum role);
}
