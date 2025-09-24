package com.apilimiter.service;

import com.apilimiter.model.User;
import com.apilimiter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), 
            user.getPasswordHash(), 
            new ArrayList<>()
        );
    }

    public User registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists!");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String apiKey = generateApiKey();

        User user = new User(email, encodedPassword, apiKey);
        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            return userOpt;
        }
        return Optional.empty();
    }

    public Optional<User> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateNewApiKey(User user) {
        String newApiKey = generateApiKey();
        user.setApiKey(newApiKey);
        userRepository.save(user);
        return newApiKey;
    }

    private String generateApiKey() {
        String apiKey;
        do {
            apiKey = "ak_" + UUID.randomUUID().toString().replace("-", "");
        } while (userRepository.existsByApiKey(apiKey));
        return apiKey;
    }
}