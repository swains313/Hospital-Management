package net.javaguides.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


	

	@GetMapping("/showMyLoginPage")
	public String showHome()
	{	
		return "login";
	}
	
//	@RequestMapping("/display")
//	public String hello()
//	{
//		System.out.println("DISPLAYYYYYYYYYYYYYYYYYYYYY");
//		return "admin/user";
//	}

	

//	@RequestMapping("/authenticateTheUser")
//	public String shwHome()
//	{
//		
//		return "user/index";
//	}
//	
//	@PostMapping("/authenticateTheUser")
//	public void shwHome()
//	{
//		
//		System.out.println("hello");
//	}


}