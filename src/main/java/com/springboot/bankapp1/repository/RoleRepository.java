package com.springboot.bankapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankapp1.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
