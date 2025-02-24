package com.mv.MVCP.controller;

import com.google.gson.Gson;
import com.mv.MVCP.dto.UserDisplayDto;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.notification.ServiceNotification.NotificationService;
import com.mv.MVCP.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notifService;

    @ModelAttribute("user")
    public UserDisplayDto addUserToModel() {
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            return userService.toUserDisplayDto(userService.findByUsername(username));
        }
        return null;
    }


    @ModelAttribute("notifications")
    public String addNotificationsToModel() {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(notifService.getNotifOfUser(userService.findByUsername(username)));
    }
}
