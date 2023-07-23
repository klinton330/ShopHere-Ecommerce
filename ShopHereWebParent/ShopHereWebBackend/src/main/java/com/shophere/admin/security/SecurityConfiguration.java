package com.shophere.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopHereUserDetailService();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
	    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	        AuthenticationManagerBuilder authenticationManagerBuilder =
	                http.getSharedObject(AuthenticationManagerBuilder.class);
	        authenticationManagerBuilder.authenticationProvider(authenticationProvider());

	        return authenticationManagerBuilder.build();
	    }
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").usernameParameter("email").permitAll());

		return httpSecurity.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/fontawesome/**", "/webfonts/**",
				"/js/**", "/webjars/**");
	}

}
