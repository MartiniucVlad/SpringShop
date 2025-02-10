package com.mv.MVCP.webSocket.chatDomain;


import com.mv.MVCP.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "chatMessage")
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    private String content;

    @Column(name = "timestamp", columnDefinition = "DATETIME")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chatRoom", nullable = false, referencedColumnName = "id")
    private ChatRoom chatRoom;

}

