package com.mv.MVCP.webSocket.chatServices;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;


public interface ChatRoomService {

    ChatRoom getChatRoom(Long roomId);

    ChatRoom getChatRoomByUserIds(Long id1, Long id2);

    ChatRoom createChatRoom(UserEntity user1, UserEntity user2);

    List<ChatMessageDto> getMessages(Long roomId);

    void saveMessage(ChatMessageDto message);

    public Map<Long, Pair<Long, Integer>> getFriendChatRoomsAndUnread(Long id);

    ChatRoom getPublicChatRoom();


    void resetNrUnread(ChatRoom privateChat);


    void incrementNrUnread(Long chatId, Long senderId);
}
