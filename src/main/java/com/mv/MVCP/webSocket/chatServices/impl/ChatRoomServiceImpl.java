package com.mv.MVCP.webSocket.chatServices.impl;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.ChatRoomType;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import com.mv.MVCP.webSocket.chatRepositories.ChatMessageRepository;
import com.mv.MVCP.webSocket.chatRepositories.ChatRoomRepository;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;




    @Override
    public ChatRoom getChatRoomByUserIds(Long id1, Long id2) {
        Optional<UserEntity> u1o = userRepository.findById(id1);
        Optional<UserEntity> u2o = userRepository.findById(id2);
        if (u1o.isEmpty() || u2o.isEmpty()) {
            return null;
        }
        UserEntity user1 = u1o.get();
        UserEntity user2 = u2o.get();

        if(user1.getId() > user2.getId()) {
            UserEntity temp = user1;
            user1 = user2;
            user2 = temp;
        }

        ChatRoom room = chatRoomRepository.findByUser1AndUser2(user1, user2);
        if (room == null) {
            return createChatRoom(user1, user2);
        }
        return room;
    }


    // user1.id must already be > than user2.id
    @Override
    public ChatRoom createChatRoom(UserEntity user1, UserEntity user2) {
        ChatRoom room = new ChatRoom();
        room.setUser1(user1);
        room.setUser2(user2);
        room.setType(ChatRoomType.PRIVATE);
        return chatRoomRepository.save(room);
    }

    @Override
    public List<ChatMessageDto> getMessages(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return null;
        }
        return chatMessageRepository.findAllByChatRoom(room).stream().map(message -> ChatMessageDto.builder()
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getUsername())
                .chatRoomId(roomId)
                .timestamp(message.getTimestamp().format(ChatMessageDto.formatter))
                .build()).toList();
    }

    @Override
    public void saveMessage(ChatMessageDto messageDto) {

        ChatMessage message = toMessage(messageDto);
       chatMessageRepository.save(message);
    }

    @Override
    public Map<Long, Long> getFriendChatRooms(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1OrUser2(user, user);
        return chatRooms.stream()
                .collect(Collectors.toMap(
                        room -> room.getUser1().getId().equals(userId) ? room.getUser2().getId() : room.getUser1().getId(),
                        ChatRoom::getId
                ));
    }

    @Override
    public ChatRoom getPublicChatRoom() {
        return chatRoomRepository.findAllByType(ChatRoomType.PUBLIC).stream().findFirst().orElse(null);
    }

    ChatMessage toMessage(ChatMessageDto chatMessageDto) {
        UserEntity user = userRepository.findById(chatMessageDto.getSenderId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + chatMessageDto.getSenderId());
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId()).orElse(null);
        if (chatRoom == null) {
            throw new RuntimeException("ChatRoom not found with ID: " + chatMessageDto.getChatRoomId());
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageDto.getContent())
                .timestamp(LocalDateTime.parse(chatMessageDto.getTimestamp(), ChatMessageDto.formatter))
                .sender(user)
                .chatRoom(chatRoom)
                .build();
        return chatMessage;
    }
}
