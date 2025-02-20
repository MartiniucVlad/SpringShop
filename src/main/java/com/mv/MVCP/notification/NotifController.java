package com.mv.MVCP.notification;


import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.notification.ServiceNotification.NotificationService;
import com.mv.MVCP.notification.domain.NotifDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotifController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @PostMapping("/resolve-friend-notification")
    public void resolveFriendNotification(@RequestBody NotifDto notifDto) {
        userService.addFriend(notifDto.getRecipient_id(), notifDto.getSender_id());
        notificationService.deleteNotif(notifDto.getId());
    }

    @PostMapping("/resolve-notification")
    public void resolveBasicNotification(@RequestBody NotifDto notifDto) {
        notificationService.deleteNotif(notifDto.getId());
    }

    
}
