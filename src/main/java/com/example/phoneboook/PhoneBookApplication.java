package com.example.phoneboook;

import com.example.phoneboook.entity.RoleEntity;
import com.example.phoneboook.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@SpringBootApplication
public class PhoneBookApplication {

    private final RoleRepository roleRepository;

    public PhoneBookApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(PhoneBookApplication.class, args);
    }

    @Component
    public class RunAfterStartup {
        @EventListener(ApplicationReadyEvent.class)
        public void runAfterStartup() {
            if (roleRepository.findAll().isEmpty()) {
                roleRepository.save(new RoleEntity("ROLE_USER"));
                roleRepository.save(new RoleEntity("ROLE_ADMIN"));
            }
        }
    }
}

