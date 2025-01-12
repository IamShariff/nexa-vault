package com.nexavault.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.nexavault.dao.UserDao;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	private final UserDao userDao;

	@Bean
	UserDetailsService userDetailsService() {
		return new SpringUserDetailsService(userDao);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).cors(cors -> {
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3001"));  // Explicitly allow frontend URL
			corsConfiguration.addAllowedMethod("*");
			corsConfiguration.addAllowedHeader("*");
			corsConfiguration.setAllowCredentials(true);
			corsConfiguration.setMaxAge(3600L);
			corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
			corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

			cors.configurationSource(request -> corsConfiguration);
		}).authorizeHttpRequests(auth -> auth
				// Allow public access to these endpoints
				.requestMatchers(PUBLIC_ENDPOINTS).permitAll()

				// Authentication required for all other endpoints
				.anyRequest().authenticated()).authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	// Define endpoints that don't require authentication
	private static final String[] PUBLIC_ENDPOINTS = { "/api/auth/**", "/api/file/{ipfsHash}", "/api/file/search",
			"/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/webjars/**", "swagger-ui/index.html", "swagger-ui/**",
			"/swagger-ui.html", "/v3/api-docs.yml", };

}