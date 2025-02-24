package com.mv.MVCP.notification;


import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.notification.ServiceNotification.NotificationService;
import com.mv.MVCP.notification.domain.Notif;
import com.mv.MVCP.notification.domain.NotifDto;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class NotifController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/resolve-friend-notification")
    public void resolveFriendNotification(@RequestBody Map<String, Object> requestBody) {
        // Extract data from the request body
        String action = (String) requestBody.get("action");
        Long senderId = Long.valueOf(requestBody.get("senderId").toString());
        Long notifId = Long.valueOf(requestBody.get("notifId").toString());

        // Fetch the notification to get the recipient ID
        Notif notif = notificationService.getNotifById(notifId);

        if (notif == null) {
            throw new RuntimeException("Notification not found");
        }

        // Handle the action (accept or reject)
        if ("accept".equals(action)) {
            // Add the friend relationship
            userService.addFriend(notif.getRecipient().getId(), senderId);
        }

        // Delete the notification
        notificationService.deleteNotif(notifId);

    }

    @PostMapping("/resolve-notification")
    public void resolveBasicNotification(@RequestBody NotifDto notifDto) {
        notificationService.deleteNotif(notifDto.getId());
    }

    //  send to /app2/add-notification
    // subscribe to /topic2/{userId}
    @MessageMapping("/add-notification")
    public ResponseEntity<String> sendNotification(@Payload NotifDto notification) {
        System.out.println("Received notification: " + notification);
        notificationService.saveNotif(notification);
        String destination = "/topic2/" + notification.getRecipient_id();
        messagingTemplate.convertAndSend(destination, notification);

        return ResponseEntity.ok("Notification sent successfully");
    }


}
