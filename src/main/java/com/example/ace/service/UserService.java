package com.example.ace.service;


import com.example.ace.domain.dto.UserDto;
import com.example.ace.domain.entity.Role;
import com.example.ace.domain.entity.User;
import com.example.ace.repository.RoleRepository;
import com.example.ace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    private final MailSender mailSender;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void createNewUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setRoles(Collections.singletonList(roleRepository.findByRoleName("ROLE_USER").orElse(new Role("ROLE_USER"))));
        user.setActivateStatus(false);
        user.setActivateCode(UUID.randomUUID().toString());
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Ace. Please, activate your account: http://localhost:6060/activate/%s",
                    user.getUsername(),
                    user.getActivateCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
        userRepository.save(user);
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivateCode(code).orElse(null);
        if (user == null) {
            return false;
        }
        user.setActivateCode(null);
        user.setActivateStatus(true);
        userRepository.save(user);
        return true;
    }

    public User findUserByEmail(String email) {
        Optional<User> userFromDb = userRepository.findByEmail(email);
        return userFromDb.orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
