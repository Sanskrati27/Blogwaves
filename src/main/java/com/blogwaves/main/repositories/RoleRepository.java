package com.blogwaves.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogwaves.main.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
