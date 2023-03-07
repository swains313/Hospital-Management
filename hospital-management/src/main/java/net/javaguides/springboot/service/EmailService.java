package net.javaguides.springboot.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {
	
//	//@Autowired
	private JavaMailSender mailSender;

	
	//@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
//	
	@Async
	public void sendEmail(SimpleMailMessage email) {
		System.out.println(email);
		mailSender.send(email);
	}
//	
	

}
