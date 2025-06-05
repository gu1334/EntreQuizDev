package br.com.entrequizdev.user.repository;

import br.com.entrequizdev.user.entity.Role;
import br.com.entrequizdev.user.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
