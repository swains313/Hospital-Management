package net.javaguides.springboot.configuration;

import java.io.IOException;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles=AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		System.out.println("CUSTOM AUTHENTI CATION ");
		
		if(roles.contains("ROLE_ADMIN")) {
			httpServletResponse.sendRedirect("/admin/user-details");
		}
		if(roles.contains("ROLE_DOCTOR")) {
			httpServletResponse.sendRedirect("/doctor/index");
		}
		if(roles.contains("ROLE_USER")) {
			httpServletResponse.sendRedirect("/user/index");
		}
		
	}

}
