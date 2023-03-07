package net.javaguides.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.entity.Admin;

@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	// @Query("select a from users a where a.username=?1")
	Admin findByEmail(String user);

	List<Admin> findByRole(String user);
	
	public void deleteByEmail(String email);

}
