package org.example.otp;

import org.example.otp.model.Role;
import org.example.otp.model.User;
import org.example.otp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpApplication.class, args);
        System.out.println("Final Try Otp Email Verification");
    }

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner setAdmin() {
        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty() ) {
                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@example.com");
                user.setPassword("admin");
                user.setRole(Role.ADMIN);
                user.setVerified(true);
                userRepository.save(user);
                System.out.println("Admin added");

            }
        };
    }

}
