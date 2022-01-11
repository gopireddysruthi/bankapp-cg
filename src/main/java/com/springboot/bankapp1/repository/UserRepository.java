package com.springboot.bankapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankapp1.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long>{

	UserInfo findByUsername(String username);
}
