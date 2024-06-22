package com.sfs.image.mgmt.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfs.image.mgmt.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	 Optional<User> findBySfsUser(String username);



}
