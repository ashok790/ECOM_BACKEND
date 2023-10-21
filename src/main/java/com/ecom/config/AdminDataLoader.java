//package com.ecom.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.ecom.model.User;
//import com.ecom.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Component
//public class AdminDataLoader implements CommandLineRunner {
//    private final UserRepository userRepository;
//    
//    private final PasswordEncoder passwordEncoder;
//
//    public AdminDataLoader(UserRepository userRepository, @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        User adminUser = new User();
//        adminUser.setEmail("admin@shopluxe.com");
//        adminUser.setPassword(passwordEncoder.encode("admin123")); // Encode the password
//        adminUser.setRole("admin");
//        adminUser.setFirstName("Admin");
//
//        userRepository.save(adminUser);
//    }
//}
//
