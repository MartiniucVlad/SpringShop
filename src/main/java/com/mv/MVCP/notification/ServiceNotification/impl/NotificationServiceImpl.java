package com.mv.MVCP.notification.ServiceNotification.impl;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.notification.ServiceNotification.NotificationService;
import com.mv.MVCP.notification.domain.Notif;
import com.mv.MVCP.notification.domain.NotifDto;
import com.mv.MVCP.notification.domain.NotifType;
import com.mv.MVCP.notification.repositoryNotifications.NotifRepository;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.ChatRoomType;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import com.mv.MVCP.webSocket.chatRepositories.ChatMessageRepository;
import com.mv.MVCP.webSocket.chatRepositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotifRepository repo;

    @Autowired
    private UserService userService;


    @Override
    public Notif getNotifById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void saveNotif(NotifDto notifDto) {
        repo.save(toNotif(notifDto));
    }

    @Override
    public void deleteNotif(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<NotifDto> getNotifOfUser(UserEntity user) {

        List<NotifDto> lst =  repo.findAllByRecipient(user).stream().map(
                notif -> NotifDto.builder()
                        .id(notif.getId())
                        .type(String.valueOf(notif.getType()))
                        .message(notif.getMessage())
                        .recipient_id(notif.getRecipient().getId())
                        .sender_id(notif.getSender().getId())
                        .sender_name(notif.getSender().getUsername())
                        .build()
        ).toList();
        return lst;
    }

    @Override
    public Notif toNotif(NotifDto notifDto) {
        return Notif.builder()
                .id(notifDto.getId())
                .type(NotifType.valueOf(notifDto.getType()))
                .sender(userService.findById(notifDto.getSender_id()))
                .recipient(userService.findById(notifDto.getRecipient_id()))
                .message(notifDto.getMessage())
                .build();
    }

    @Override
    public NotifDto toNotifDto(Notif notif) {
        return NotifDto.builder()
                .id(notif.getId())
                .type(String.valueOf(notif.getType()))
                .message(notif.getMessage())
                .recipient_id(notif.getRecipient().getId())
                .sender_id(notif.getSender().getId())
                .sender_name(notif.getSender().getUsername())
                .build();
    }


}
