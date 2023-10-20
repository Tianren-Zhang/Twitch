package com.laioffer.twitch.user;

import com.laioffer.twitch.db.UserRepository;
import com.laioffer.twitch.db.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // registers a user.
    // A new UserDetails object is created with the given username and encoded password.
    // The .roles("USER") part assigns the role "USER" to this user. This role can be used by Spring Security for authorization decisions.
    // The UserDetailsManager saves this new user.
    // The userRepository updates the first and last name for this user.
    public void register(String username, String password, String firstName, String lastName) {
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        userRepository.updateNameByUsername(username, firstName, lastName);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}