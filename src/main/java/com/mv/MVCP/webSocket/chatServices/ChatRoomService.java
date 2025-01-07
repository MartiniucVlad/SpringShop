package com.mv.MVCP.webSocket.chatServices;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ChatRoomService {

    ChatRoom getChatRoom(Long id1, Long id2);

    ChatRoom createChatRoom(UserEntity user1, UserEntity user2);

    List<ChatMessage> getMessages(Long roomId);

    void addMessage(ChatMessage message);

    List<ChatRoom> getUserChatRooms(Long id);
}
