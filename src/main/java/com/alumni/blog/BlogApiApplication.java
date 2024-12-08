package com.alumni.blog;

import com.alumni.blog.entities.Role;
import com.alumni.blog.entities.User;
import com.alumni.blog.repository.RoleRepo;
import com.alumni.blog.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BlogApiApplication implements CommandLineRunner {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepo.findById(1).isEmpty()) {
            Role userRole = new Role();
            userRole.setId(1);
            userRole.setName("ROLE_USER");
            roleRepo.save(userRole);
        }

        if (roleRepo.findById(2).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setId(2);
            adminRole.setName("ROLE_ADMIN");
            roleRepo.save(adminRole);
        }
        // Check if any admin exists
        boolean adminExists = userRepo.existsByRoles_Name("ROLE_ADMIN");
        if (!adminExists) {
            // Create the first admin
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("securePassword")); // Replace with a strong password
            admin.setAbout("The first admin user");

            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseThrow(() ->
                    new RuntimeException("ROLE_ADMIN not found in the database."));
            admin.getRoles().add(adminRole);

            userRepo.save(admin);
            System.out.println("First admin user initialized: admin@example.com");
        }
    }
        public static void main (String[]args){
            SpringApplication.run(BlogApiApplication.class, args);
        }
        @Bean
        public ModelMapper modelMapper ()
        {
            return new ModelMapper();
        }

    }

