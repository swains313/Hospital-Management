package net.javaguides.springboot.controller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.javaguides.springboot.entity.Admin;
import net.javaguides.springboot.entity.Appointment;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.service.AdminServiceImplementation;
import net.javaguides.springboot.service.AppointmentServiceImplementation;
import net.javaguides.springboot.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AdminServiceImplementation adminServiceImplementation;

	@Autowired
	private AppointmentServiceImplementation appointmentServiceImplementation;

	public AdminController(UserService userService, AdminServiceImplementation obj,
			AppointmentServiceImplementation app) {
		this.userService = userService;
		adminServiceImplementation = obj;
		appointmentServiceImplementation = app;
	}

	@GetMapping("/user-details")
	public String index(Model model) {

		List<Admin> list = adminServiceImplementation.findByRole("ROLE_USER");
		model.addAttribute("user", list);

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		return "admin/user";
	}

	@GetMapping("/doctor-details")
	public String doctorDetails(Model model) {

		System.out.println("DOCTOR DETAILS ");

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

	//	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		List<Admin> list = adminServiceImplementation.findByRole("ROLE_DOCTOR");

		// add to the spring model
		model.addAttribute("user", list);

		System.out.println("DOCTOR DETAILS END");

		return "admin/doctor";
	}

	@GetMapping("/admin-details")
	public String adminDetails(Model model) {

		System.out.println("ADMIN DETAILS IT COMING");

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

	//	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		List<Admin> list = adminServiceImplementation.findByRole("ROLE_ADMIN");

		// add to the spring model
		model.addAttribute("user", list);

		System.out.println("ADMIN DETAILS END");

		return "admin/admin";
	}

	@GetMapping("/add-doctor")
	public String showFormForAdd(Model theModel) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin1 = adminServiceImplementation.findByEmail(username);

		//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin1.setLastseen(log);

		adminServiceImplementation.save(admin1);

		// create model attribute to bind form data
		Admin admin = new Admin();

		theModel.addAttribute("doctor", admin);

		return "admin/addDoctor";
	}

	@PostMapping("/save-doctor")
	public String saveEmployee(@ModelAttribute("doctor") Admin admin) {

		// save the employee
		// admin.setId(0);

		admin.setRole("ROLE_DOCTOR");

		admin.setPassword(passwordEncoder.encode(admin.getPassword()));

		admin.setEnabled(true);

		System.out.println(admin.getPassword() + " " + passwordEncoder.encode(admin.getPassword()));

		admin.setConfirmationToken("ByAdmin-Panel");

		//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		System.out.println(admin);

		// admin.setPassword()

		User user = new User();
		user.setRole(admin.getRole());
		user.setFirstName(admin.getFirstName());
		user.setLastName(admin.getLastName());
		user.setLastseen(admin.getLastseen());
		user.setEmail(admin.getEmail());
		user.setGender(admin.getGender());
		user.setRole(admin.getRole());
		user.setPassword(admin.getPassword());
		user.setConfirmationToken("ByAdmin-Panel");
		user.setEnabled(true);

		userService.saveUser(user);

		adminServiceImplementation.save(admin);
		System.out.println("SAVE-DOCTOR");

		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/doctor-details";
	}

	@GetMapping("/add-admin")
	public String showForm(Model theModel) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin1 = adminServiceImplementation.findByEmail(username);

		//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		System.out.println("log" + "ADMIN/ADD-ADMIN");

		admin1.setLastseen(log);

		adminServiceImplementation.save(admin1);

		System.out.println("ADD-ADMIN");

		// create model attribute to bind form data
		Admin admin = new Admin();

		theModel.addAttribute("doctor", admin);

		return "admin/addAdmin";
	}

	@PostMapping("/save-admin")
	public String saveEmploye(@ModelAttribute("doctor") Admin admin) {

		// save the employee
		// admin.setId(0);

		admin.setRole("ROLE_ADMIN");
		System.out.println(admin.getEmail() + " " + admin.getPassword());
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		admin.setEnabled(true);
		admin.setConfirmationToken("ByAdmin-Panel");
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		Date date=new Date();
		admin.setLastseen(date.toString());
		
		User user=new User();
		user.setRole(admin.getRole());
		user.setFirstName(admin.getFirstName());
		user.setLastName(admin.getLastName());
		user.setLastseen(admin.getLastseen());
		user.setEmail(admin.getEmail());
		user.setGender(admin.getGender());
		user.setRole(admin.getRole());
		user.setPassword(admin.getPassword());
		user.setConfirmationToken("ByAdmin-Panel");
		user.setEnabled(true);
		
		userService.saveUser(user);

		

		adminServiceImplementation.save(admin);

		System.out.println("SAVE-ADMIN");
		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/admin-details";
	}

	@GetMapping("/edit-my-profile")
	public String EditForm(Model theModel) {

		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);
		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		// get the employee from the service

		Admin admin = adminServiceImplementation.findByEmail(username);

	//	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		System.out.println(admin);

		theModel.addAttribute("profile", admin);

		return "admin/updateMyProfile";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("profile") Admin admin) {

		System.out.println(admin);

		adminServiceImplementation.save(admin);

		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/user-details";
	}


	@GetMapping("/appointments")
	public String appointments(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		List<Appointment> list = appointmentServiceImplementation.findAll();

		// add to the spring model
		model.addAttribute("app", list);

		System.out.println("APPOINTMENT");

		return "admin/appointment";
	}
	
	
	@GetMapping("/delete/{email}")
	public String deleteById(@PathVariable String email)
	{
	adminServiceImplementation.deleteByEmail(email);
	userService.deleteByEmail(email);
	 //System.out.println(ad.getEmail());
		return "redirect:/admin/user-details";
	}

}
