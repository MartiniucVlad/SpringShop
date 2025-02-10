package com.mv.MVCP.webSocket.chatServices;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;

import java.util.List;


public interface ChatRoomService {

    ChatRoom getChatRoomByUserIds(Long id1, Long id2);

    ChatRoom createChatRoom(UserEntity user1, UserEntity user2);

    List<ChatMessage> getMessages(Long roomId);

    void saveMessage(ChatMessageDto message);

    List<ChatRoom> getUserChatRooms(Long id);

    ChatRoom getPublicChatRoom();
}
