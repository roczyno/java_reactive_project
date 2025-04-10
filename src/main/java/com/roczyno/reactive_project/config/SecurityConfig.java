package com.roczyno.reactive_project.config;

import com.roczyno.reactive_project.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

private final JwtService jwtService;

	public SecurityConfig(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Bean
	SecurityWebFilterChain httpSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager){
		return http.authorizeExchange(exchanges->exchanges
						.pathMatchers(HttpMethod.POST,"/users/**").permitAll()
						.pathMatchers(HttpMethod.GET,"/users/stream").permitAll()
						.pathMatchers(HttpMethod.POST,"/auth/**").permitAll()
				.anyExchange().authenticated())
				.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.authenticationManager(authenticationManager)
				.addFilterAt(new JwtAuthenticationFilter(jwtService), SecurityWebFiltersOrder.AUTHENTICATION)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	private CorsConfigurationSource corsConfigurationSource(){
		CorsConfiguration configuration= new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081","sgjskg"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",configuration);
		return source;
	}
}

