package com.klasix12.repository;

import com.klasix12.dto.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<com.klasix12.model.Role, Long> {
    com.klasix12.model.Role findByName(Role name);
}
