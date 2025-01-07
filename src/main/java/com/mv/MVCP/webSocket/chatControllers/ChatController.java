package com.mv.MVCP.webSocket.chatControllers;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    UserService userService;




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

    //  to /app/chat
    @MessageMapping("/chat")
    // redirect to
    @SendTo("/user/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        chatRoomService.addMessage(message);
        message.setTimestamp(new java.util.Date().toString());
        return message;
    }

}
