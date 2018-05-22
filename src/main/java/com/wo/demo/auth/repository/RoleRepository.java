package com.wo.demo.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wo.demo.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}