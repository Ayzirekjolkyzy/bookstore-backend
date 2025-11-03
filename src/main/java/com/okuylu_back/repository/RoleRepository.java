package com.okuylu_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okuylu_back.security.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
