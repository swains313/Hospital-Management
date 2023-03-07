package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.entity.Admin;





public interface AdminService {
	
	public List<Admin> findByRole(String user);
	
	public Admin findByEmail(String user);
	
	public List<Admin> findAll();
	
	public void save(Admin admin);
	
	public void deleteByEmail(String email);

}
