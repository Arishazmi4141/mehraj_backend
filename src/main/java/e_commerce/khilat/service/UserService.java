package e_commerce.khilat.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.khilat.entity.User;
import e_commerce.khilat.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	
	
	
	public User login(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
	
	// SIGNUP
    public User register(User user) {

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }
    
    

}
