package com.mv.MVCP.Service;

import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    UserEntity findByEmail(String email);
    UserEntity findByUser(String user);
}
