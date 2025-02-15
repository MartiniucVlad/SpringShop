package com.mv.MVCP.notification.ServiceNotification.impl;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.notification.ServiceNotification.NotificationService;
import com.mv.MVCP.notification.domain.Notif;
import com.mv.MVCP.notification.domain.NotifDto;
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


    @Override
    public void saveNotif(Notif notif) {
        repo.save(notif);
    }

    @Override
    public void deleteNotif(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<NotifDto> getNotifOfUser(UserEntity user) {

        List<NotifDto> lst =  repo.findAllByRecipient(user).stream().map(
                notif -> NotifDto.builder()
                        .type(String.valueOf(notif.getType()))
                        .message(notif.getMessage())
                        .recipient_id(notif.getRecipient().getId())
                        .sender_id(notif.getSender().getId())
                        .sender_name(notif.getSender().getUsername())
                        .build()
        ).toList();
        return lst;
    }


}
