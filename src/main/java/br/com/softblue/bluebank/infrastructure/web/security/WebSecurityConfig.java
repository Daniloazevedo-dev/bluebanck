package br.com.softblue.bluebank.infrastructure.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {

	http.csrf().disable().cors()
	.and()
		.headers().frameOptions().disable()
	.and()
		.httpBasic()
	.and()
		.authorizeRequests()
			.antMatchers("/public/**","/_imagens/**", "/_css/**", "/_js/**", "/index/**").permitAll()
			.anyRequest().authenticated()
		.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
	.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager()))
	.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	logger.info("Security Setup...Ok!");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/login")
		.allowedOrigins("*")
		.allowedMethods("POST")
		.exposedHeaders(SecurityConstants.AUTHORIZATION_HEADER);
	
	logger.info("CORS...Setup!");
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

}
