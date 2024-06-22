package com.sfs.image.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfs.image.mgmt.entity.Image;
import com.sfs.image.mgmt.entity.User;

public interface ImageRepository extends JpaRepository<Image, Long>{
	 List<Image> findByUser(User user);
	
}
