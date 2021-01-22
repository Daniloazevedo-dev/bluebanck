package br.com.softblue.bluebank.infrastructure.web.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    
    public static String userDetailsImpl() {
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	System.out.println(authentication.getPrincipal());
	
	
	if(authentication instanceof AnonymousAuthenticationToken) {
		return null;
	}
	
	return  (String) authentication.getPrincipal();
	
    }
    
    
}
