package com.alumni.blog.payloads;

import com.alumni.blog.entities.Role;
import com.alumni.blog.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
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
    }
}
