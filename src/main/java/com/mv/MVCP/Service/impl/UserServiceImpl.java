package com.mv.MVCP.Service.impl;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.RoleEntity;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.RoleRepository;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Lazy
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

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
    public UserEntity findByUsername(String user) {
        return userRepository.findByUsername(user);
    }

    @Override
    public void changeStatus(UserEntity user, String type) {
       user.setStatusType(type);
       userRepository.save(user);
    }

    @Override
    public List<UserEntity> getFriendList(UserEntity user) {
        return userRepository.findFullFriendList(user.getId());
    }

    @Override
    public void addFriend(Long userId1, Long userId2) {
        UserEntity user1 = userRepository.findById(userId1).orElse(null);
        UserEntity user2 = userRepository.findById(userId2).orElse(null);
        if(user1 ==  null || user2 == null)
                return;
        if(user1.getId() > user2.getId()) {
            UserEntity temp = user1;
            user1 = user2;
            user2 = temp;
        }
        if (user1.getPartialFriendList().contains(user2)) {
           return;
        }

        user1.getPartialFriendList().add(user2);
        userRepository.save(user1);

        chatRoomService.createChatRoom(user1, user2);
    }

    @Override
    public void removeFriend(Long userId1, Long userId2) {
        UserEntity user1 = userRepository.findById(userId1).orElse(null);
        UserEntity user2 = userRepository.findById(userId2).orElse(null);

        if(user1 ==  null || user2 == null)
            return;
        if(user1.getId() > user2.getId()) {
            UserEntity temp = user1;
            user1 = user2;
            user2 = temp;
        }

        user1.getPartialFriendList().remove(user2);
        userRepository.save(user1);
    }






}
