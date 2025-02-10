package com.mv.MVCP.webSocket.chatControllers;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    UserService userService;


    //  to /app/chat
    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessagePublic(@Valid @Payload ChatMessageDto messageDto) {
    //    chatRoomService.saveMessage(messageDto); ignore this comment

        LocalDateTime now = LocalDateTime.now();
        // Format the timestamp to include day and time (hour and minute)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTimestamp = now.format(formatter);
        messageDto.setTimestamp(formattedTimestamp);
        return messageDto;
    }

    @GetMapping("public-chat")
    public String publicChat(Model model) {
        model.addAttribute("publicChatId", chatRoomService.getPublicChatRoom().getId());
        return "public-chat";
    }




    public UserEntity getUser () {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUser(username);
            return user;
        }
        return null;
    }


    @ModelAttribute("userChatRooms")
    public List<ChatRoom> addUserChatRooms() {
        UserEntity user = getUser();
        if (user != null) {
            return chatRoomService.getUserChatRooms(user.getId());
        }
        return null;
    }




}
