package com.mv.MVCP.webSocket.chatServices;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;

import java.util.List;
import java.util.Map;


public interface ChatRoomService {

    ChatRoom getChatRoomByUserIds(Long id1, Long id2);

    ChatRoom createChatRoom(UserEntity user1, UserEntity user2);

    List<ChatMessageDto> getMessages(Long roomId);

    void saveMessage(ChatMessageDto message);

    public Map<Long, Long> getFriendChatRooms(Long id);

    ChatRoom getPublicChatRoom();


}
