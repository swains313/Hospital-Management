package net.javaguides.springboot.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	
	@Autowired
	private DataSource securityDataSource;
	

//
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	protected void filterChain(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(securityDataSource)
		.usersByUsernameQuery("select username,password,enabled from users where username=?")
		.authoritiesByUsernameQuery("select username, authority from users where username=?")
		.passwordEncoder(passwordEncoder());
    }
	
	@Bean
    protected PasswordEncoder passwordEncoder() {
		System.out.println("PASSWORD ENCODER");
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
		System.out.println("SRCURITY CHAIN START");
	
		http.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/register").permitAll()
		.requestMatchers("/confirm").permitAll()
		.requestMatchers("/login/**").permitAll()
		.requestMatchers("/css/**").permitAll()
		.requestMatchers("/js/***").permitAll()
		.requestMatchers("/static/**").permitAll()
		.requestMatchers("/vendor/**").permitAll()
		.requestMatchers("resources/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/showMyLoginPage")
		.loginProcessingUrl("/authenticateTheUser")
		//.defaultSuccessUrl("/register")
		.permitAll()
		.successHandler(customAuthenticationSuccessHandler)
	.and()
	.logout().permitAll()
	.and()
	.exceptionHandling().accessDeniedPage("/register");
		;
		
		return http.build();
	}
	
	protected void filterChain(WebSecurity web)throws Exception {		
		web.ignoring().requestMatchers("/resources/**","/login/**","/static/**","/Script/**",
				"/Style/**","/Icon/**","/js/**","/vendor/**","/bootstrap/**","/Image/**");
		
	}
	
	@Bean
	protected UserDetailsManager userDetailsManager() {
		System.out.println("USER DETALMANAGER START (SECURITY CONFIGURATION)");
		JdbcUserDetailsManager jdbcUserDetailsManager=new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(securityDataSource);
		System.out.println("USER DETAIL MANAGER END (SECURITY CONFIGURATION)");
		
		return jdbcUserDetailsManager;
	}	
	
}

class PasswordEncoderTest implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}
	
}
