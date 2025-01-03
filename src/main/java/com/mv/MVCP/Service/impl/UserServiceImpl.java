package com.mv.MVCP.Service.impl;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.RoleEntity;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.RoleRepository;
import com.mv.MVCP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        RoleEntity role = roleRepository.findByName("user");
        user.addRole(role);

        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public UserEntity findByUser(String user) {
        return userRepository.findByUsername(user);
    }




}
