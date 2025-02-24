package com.mv.MVCP.webSocket.chatControllers;

import com.google.gson.Gson;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import com.mv.MVCP.webSocket.chatDomain.ChatRoom;
import com.mv.MVCP.webSocket.chatDomain.dto.ChatMessageDto;
import com.mv.MVCP.webSocket.chatServices.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    UserService userService;


    //  to /app/chat
    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessagePublic(@Valid @Payload ChatMessageDto messageDto) {
        messageDto.setTimestamp(LocalDateTime.now().format(ChatMessageDto.formatter));
        chatRoomService.saveMessage(messageDto);
        return messageDto;
    }

        @GetMapping("public-chat")
        public String publicChat(Model model) {
            ChatRoom publicChat = chatRoomService.getPublicChatRoom();
            model.addAttribute("publicChatId", publicChat.getId());
            Gson gson = new Gson();
            String publicMessagesJson = gson.toJson(chatRoomService.getMessages(publicChat.getId()));
            model.addAttribute("publicMessages", publicMessagesJson);
          //  System.out.print("publicMessages: " + chatRoomService.getMessages(publicChat.getId()).size());
            return "public-chat";
        }


    List<Map<String, Object>> getFriendsChatRoomList(UserEntity currUser) {
        Map<Long, Pair<Long, Integer>> details = chatRoomService.getFriendChatRoomsAndUnread(currUser.getId());
        System.out.println(details);

        return userService.getFriendList(currUser).stream()
                .map(friend -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", friend.getId());
                    map.put("username", friend.getUsername());
                    map.put("chatRoomId", details.get(friend.getId()).getFirst()); // Get chat room ID from the preloaded map
                    map.put("nrUnreadUser", details.get(friend.getId()).getSecond()); // Get nr of unread messages
                    System.out.println("nrUnreadUser: " + details.get(friend.getId()).getSecond());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("friends-chat")
    public String friendsChat(Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity currUser = userService.findByUsername(username);


        Gson gson = new Gson();
        String friendListJson = gson.toJson(getFriendsChatRoomList(currUser));
        model.addAttribute("friendList", friendListJson);
        return "friends-chat";
    }

    @MessageMapping("/private-chat/{chatRoomId}")
    @SendTo("/topic/private/{chatRoomId}")
    public ChatMessageDto sendMessagePrivate(@DestinationVariable Long chatRoomId, @Valid @Payload ChatMessageDto messageDto) {
        messageDto.setTimestamp(LocalDateTime.now().format(ChatMessageDto.formatter));

        chatRoomService.incrementNrUnread(chatRoomId, messageDto.getSenderId());
        chatRoomService.saveMessage(messageDto);
        return messageDto;
    }

    @GetMapping("friends-chat/{friendId}")
    @ResponseBody
    public Map<String, Object>  privateChat(@PathVariable Long friendId) {
        String username = SecurityUtil.getSessionUser();
        UserEntity currUser = userService.findByUsername(username);

        ChatRoom privateChat = chatRoomService.getChatRoomByUserIds(friendId, currUser.getId());

        chatRoomService.resetNrUnread(privateChat);

        Gson gson = new Gson();
        String privateMessagesJson = gson.toJson(chatRoomService.getMessages(privateChat.getId()));

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", privateChat.getId());
        response.put("privateMessages", privateMessagesJson);

        return response;
    }



    public UserEntity getUser () {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            return user;
        }
        return null;
    }


//    @ModelAttribute("userChatRooms")
//    public List<ChatRoom> addUserChatRooms() {
//        UserEntity user = getUser();
//        if (user != null) {
//            return chatRoomService.getUserChatRooms(user.getId());
//        }
//        return null;
//    }




}
