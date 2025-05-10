package org.example.server.config;

import org.example.server.dto.AccountType;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AdminInitializer {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	@Transactional
	public CommandLineRunner initAdmin() {
		return args -> {
			try {
				if (userRepository.findByEmail("admin@example.com") == null) {
					String encodedPassword = passwordEncoder.encode("admin");

					User adminUser = new User(
							"admin@example.com",
							encodedPassword,
							"Administrator",
							AccountType.Admin.ordinal());

					userRepository.save(adminUser);
					System.out.println("Admin user created successfully");
				} else {
					System.out.println("Admin user already exists");
				}
			} catch (Exception e) {
				System.err.println("Admin Initializer Error: " + e.getMessage());
			}
		};
	}
}