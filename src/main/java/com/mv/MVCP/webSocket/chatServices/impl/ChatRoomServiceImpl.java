package com.mv.MVCP.webSocket.chatServices.impl;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import com.mv.MVCP.webSocket.chatDomain.ChatMessage;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.ChatRoomType;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import com.mv.MVCP.webSocket.chatRepositories.ChatMessageRepository;
import com.mv.MVCP.webSocket.chatRepositories.ChatRoomRepository;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserService userService;


    @Override
    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }


    @Override
    public ChatRoom getChatRoomByUserIds(Long id1, Long id2) {

        UserEntity user1 = userService.findById(id1);
        UserEntity user2 = userService.findById(id2);

        if (user1 == null || user2 == null) {
            return null;
        }

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
        if(getChatRoomByUserIds(user1.getId(), user2.getId()) != null) {
            return null;
        }

        ChatRoom room = new ChatRoom();
        room.setUser1(user1);
        room.setUser2(user2);
        room.setType(ChatRoomType.PRIVATE);
        room.setNrUnreadUser1(0);
        room.setNrUnreadUser2(0);
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
    public Map<Long, Pair<Long, Integer>> getFriendChatRoomsAndUnread(Long userId) {
        UserEntity user = userService.findById(userId);
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1OrUser2(user, user);

        return chatRooms.stream()
                .collect(Collectors.toMap(
                        room -> room.getUser1().getId().equals(userId) ? room.getUser2().getId() : room.getUser1().getId(),
                        room -> Pair.of(room.getId(),
                                room.getUser1().getId().equals(userId) ? room.getNrUnreadUser1() : room.getNrUnreadUser2())
                ));
    }

    @Override
    public ChatRoom getPublicChatRoom() {
        return chatRoomRepository.findAllByType(ChatRoomType.PUBLIC).stream().findFirst().orElse(null);
    }

    ChatMessage toMessage(ChatMessageDto chatMessageDto) {
        UserEntity user = userService.findById(chatMessageDto.getSenderId());
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

    @Override
    public void resetNrUnread(ChatRoom privateChat) {
            String username = SecurityUtil.getSessionUser();
            UserEntity currUser = userService.findByUsername(username);

            if(currUser.equals(privateChat.getUser1())) {
                privateChat.setNrUnreadUser1(0);
            }
            else
                privateChat.setNrUnreadUser2(0);
            chatRoomRepository.save(privateChat);
        }

        @Override
        public void incrementNrUnread(Long chatId, Long senderId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElse(null);

        if(!senderId.equals(chatRoom.getUser1().getId())) {
            chatRoom.setNrUnreadUser1(chatRoom.getNrUnreadUser1() + 1);
        }
        else
            chatRoom.setNrUnreadUser2(chatRoom.getNrUnreadUser2() + 1);

        chatRoomRepository.save(chatRoom);
    }


}
