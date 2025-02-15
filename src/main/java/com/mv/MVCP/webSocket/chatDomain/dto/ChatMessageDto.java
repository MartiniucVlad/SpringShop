package com.mv.MVCP.webSocket.chatDomain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotEmpty
    private String content;

    @NotNull
    private Long senderId;

    @NotEmpty
    private String senderName;

    private String timestamp;


    @NotNull
    private Long chatRoomId; // ID to identify the chat room


}
