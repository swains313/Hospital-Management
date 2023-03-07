package net.javaguides.springboot.controller;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.entity.Admin;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.service.AdminServiceImplementation;
import net.javaguides.springboot.service.EmailService;
import net.javaguides.springboot.service.UserService;

@RestController
public class RegisterController {

	// private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	AdminServiceImplementation adminServiceImplementation;

	@Autowired
	PasswordEncoder encoder;

	public RegisterController(UserService userService, EmailService emailService) {
		// this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
		this.emailService = emailService;
	}

	// Return registration form template
	@GetMapping("/register")
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;
	}

	// Process form input data
	@PostMapping("/register")
	public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Validated User user,
			BindingResult bindingResult, HttpServletRequest request) {

		// Lookup user in database by e-mail
		User userExists = userService.findByEmail(user.getEmail());

		System.out.println(userExists);

		if (userExists != null) {
			modelAndView.addObject("alreadyRegisteredMessage",
					"Oops!  There is already a user registered with the email provided.");
			modelAndView.setViewName("register");
			bindingResult.reject("email");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("register");
		} else { // new user so we create user and send confirmation e-mail

			// Disable user until they click on confirmation link in email

			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setGender(user.getGender());
			user.setPassword(encoder.encode(user.getPassword()));

			Date now = new Date();

			String log = now.toString();

			user.setLastseen(log);

			// Generate random 36-character string token for confirmation link
			user.setConfirmationToken(UUID.randomUUID().toString());

			Admin admin = new Admin();
			//NOTE IMP
			admin.setEnabled(user.isEnabled());
			admin.setRole(user.getRole());
			admin.setGender(user.getGender());
			admin.setPassword(user.getPassword());
			admin.setConfirmationToken(user.getConfirmationToken());
			admin.setLastName(user.getLastseen());
			admin.setFirstName(user.getFirstName());
			admin.setLastName(user.getLastName());
			admin.setEmail(user.getEmail());
			admin.setLastseen(user.getLastseen());

			adminServiceImplementation.save(admin);

			userService.saveUser(user);

			// String appUrl = request.getScheme() + "://" + request.getServerName();

			// String appUrl = "localhost:8080";

			SimpleMailMessage registrationEmail = new SimpleMailMessage();
			registrationEmail.setTo(user.getEmail());
			registrationEmail.setSubject("Registration Confirmation");
			registrationEmail.setText("WELCOME TO RE:MEDIC MEDICAL \n THANKS FOR REGISTER ");
			registrationEmail.setFrom("communitydevloper@gmail.com");

			System.out.println(registrationEmail);
			

			emailService.sendEmail(registrationEmail);

			modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
			modelAndView.setViewName("register");
		}

		return modelAndView;
	}

	// Process confirmation link
	@GetMapping("/confirm")
	public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {

		User user = userService.findByConfirmationToken(token);

		if (user == null) { // No token found in DB
			modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
		} else { // Token found
			modelAndView.addObject("confirmationToken", user.getConfirmationToken());
		}

		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	// Process confirmation link
	@PostMapping("/confirm")
	public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

		modelAndView.setViewName("confirm");

		Zxcvbn passwordCheck = new Zxcvbn();

		Strength strength = passwordCheck.measure(requestParams.get("password"));

		if (strength.getScore() < 3) {
			// modelAndView.addObject("errorMessage", "Your password is too weak. Choose a
			// stronger one.");
			bindingResult.reject("password");

			redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

			modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
			System.out.println(requestParams.get("token"));
			return modelAndView;
		}

		// Find the user associated with the reset token
		User user = userService.findByConfirmationToken(requestParams.get("token"));

		// Set new password
		// user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
		user.setPassword(requestParams.get("password"));

		// Set user to enabled
		user.setEnabled(true);

		// Save user
		userService.saveUser(user);

		modelAndView.addObject("successMessage", "Your password has been set!");
		return modelAndView;
	}
	
	@PostMapping("/updateSave/{email}")
	public String updateSave(@PathVariable String email, @ModelAttribute("user") User u)
	{
		System.err.println("Error OCCURED 1");
		Admin admin=adminServiceImplementation.findByEmail(email);
		System.err.println("Error OCCURED 2");
		admin.setFirstName(u.getFirstName());
		admin.setLastName(u.getLastName());
		admin.setGender(u.getGender());
		admin.setEmail(u.getEmail());
		admin.setPassword(encoder.encode(u.getPassword()));
		
		System.err.println("Error OCCURED 3");
		User user=userService.findByEmail(email);
		System.err.println("Error OCCURED 4");
		
		user.setRole(u.getRole());
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setLastseen(u.getLastseen());
		user.setEmail(u.getEmail());
		user.setGender(u.getGender());
		user.setRole(u.getRole());
		user.setPassword(encoder.encode(u.getPassword()));
		user.setConfirmationToken("ByAdmin-Panel");
		user.setEnabled(true);
		System.err.println("Error OCCURED 5");
		
		
		
		System.out.println(user.getEmail());
		System.out.println(admin.getEmail());
//		
		
		
		
		userService.saveUser(user);
		adminServiceImplementation.save(admin);
		
		return "redirect:/admin/user-details";
	}

}
