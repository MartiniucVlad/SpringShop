package com.mv.MVCP.Service;

import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserEntity findById(Long id);

    void saveUser(RegistrationDto registrationDto);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String user);

    void changeStatus(UserEntity user, String type);

    List<UserEntity> getFriendList(UserEntity user);

    void addFriend(Long userId1, Long userId2);

    void removeFriend(Long userId1, Long userId2);
}
