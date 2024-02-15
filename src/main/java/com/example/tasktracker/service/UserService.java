package com.example.tasktracker.service;

import com.example.tasktracker.dto.RegistrationRequest;
import com.example.tasktracker.entity.Authority;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.repository.AuthorityRepository;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    public User save(RegistrationRequest registrationRequest) {
        User user = userMapper.registrationDtoToUser(registrationRequest);
        Authority e1 = new Authority();
        e1.setName("ROLE_USER");
        user.setAuthorities(Set.of(e1));
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        return userRepository.save(user);
    }

}
