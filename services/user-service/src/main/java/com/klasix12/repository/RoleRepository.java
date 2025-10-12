package com.klasix12.repository;

import com.klasix12.dto.RoleName;
import com.klasix12.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
