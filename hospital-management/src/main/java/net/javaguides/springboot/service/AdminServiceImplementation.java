package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.entity.Admin;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.AdminRepository;

@Service
public class AdminServiceImplementation implements AdminService {

	@Autowired
	private AdminRepository adminRepository;


	// @Autowired //Adding bean id @Qualifier
	public AdminServiceImplementation(AdminRepository obj) {
		adminRepository = obj;
	}

	@Override
	public List<Admin> findAll() {
		return adminRepository.findAll();
	}

	@Override
	public void save(Admin admin) {

		adminRepository.save(admin);
	}

	@Override
	public Admin findByEmail(String user) {
		
		

		List<Admin> list = adminRepository.findAll();

		for (Admin admin : list) {

		}
		for (Admin admin : list) {
			if (user.equals(admin.getEmail())) {
				return admin;
			}

		}

		return null;

	}

	@Override
	public List<Admin> findByRole(String user) {
		return adminRepository.findByRole(user);
	}

	@Override
	public void deleteByEmail(String email) {
		List<Admin> list=adminRepository.findAll();
		for (Admin admin : list) {
			if (email.equals(admin.getEmail())) {
				adminRepository.deleteById(admin.getId());
			}
		}
	}

}
