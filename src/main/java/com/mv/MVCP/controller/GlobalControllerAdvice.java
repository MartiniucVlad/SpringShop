package com.mv.MVCP.controller;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserEntity addUserToModel() {
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            return userService.findByUser(username);
        }
        return null;
    }
}
