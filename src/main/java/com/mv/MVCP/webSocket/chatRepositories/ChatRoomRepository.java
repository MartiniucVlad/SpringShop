package com.mv.MVCP.webSocket.chatRepositories;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findById(long id);

    List<ChatRoom> findAllByUser1(UserEntity user1);
    ChatRoom findByUser1AndUser2(UserEntity user1, UserEntity user2);
    List<ChatRoom> findAllByUser1OrUser2(UserEntity user1, UserEntity user2);


    List<ChatRoom> findAllByType(ChatRoomType chatRoomType);
}
