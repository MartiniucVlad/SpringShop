package com.mv.MVCP.webSocket.chatServices.impl;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatRepositories.ChatMessageRepository;
import com.mv.MVCP.webSocket.chatRepositories.ChatRoomRepository;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public ChatRoom getChatRoom(Long id1, Long id2) {
        Optional<UserEntity> u1 = userRepository.findById(id1);
        Optional<UserEntity> u2 = userRepository.findById(id2);
        if (u1.isEmpty() || u2.isEmpty()) {
            return null;
        }

        ChatRoom room = chatRoomRepository.findByUser1AndUser2(u1.get(), u2.get());
        if (room == null) {
            return createChatRoom(u1.get(), u2.get());
        }
        return room;
    }


    @Override
    public ChatRoom createChatRoom(UserEntity user1, UserEntity user2) {
        ChatRoom room = new ChatRoom();
        room.setUser1(user1);
        room.setUser2(user2);
        return chatRoomRepository.save(room);
    }

    @Override
    public List<ChatMessage> getMessages(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return null;
        }
        return chatMessageRepository.findAllByChatRoom(room);
    }

    @Override
    public void addMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    @Override
    public List<ChatRoom> getUserChatRooms(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return chatRoomRepository.findAllByUser1OrUser2(user, user);
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
