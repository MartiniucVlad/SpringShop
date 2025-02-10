package com.mv.MVCP.webSocket;

import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.ChatRoomType;
import com.mv.MVCP.webSocket.chatRepositories.ChatRoomRepository;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

   @Autowired
   private ChatRoomRepository chatRoomRepository;

    @PostConstruct
    public void init() {
        if (chatRoomRepository.findAllByType(ChatRoomType.PUBLIC).isEmpty()) {
            ChatRoom publicRoom = new ChatRoom();
            publicRoom.setType(ChatRoomType.PUBLIC);
            chatRoomRepository.save(publicRoom);
        }
    }
}
