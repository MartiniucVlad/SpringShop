package com.mv.MVCP.webSocket.chatDomain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {
    @NotEmpty
    private String content;

    @NotNull
    private Long senderId;

    @NotEmpty
    private String senderName;

    String timestamp;

    @NotNull
    private Long chatRoomId; // ID to identify the chat room


}
