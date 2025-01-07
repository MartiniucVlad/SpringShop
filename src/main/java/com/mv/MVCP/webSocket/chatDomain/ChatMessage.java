package com.mv.MVCP.webSocket.chatDomain;


import com.mv.MVCP.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "chatMessage")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String sender;
    @NotEmpty
    private String recipient;
    @NotEmpty
    private String content;
    @NotEmpty
    private String timestamp;

    //chatRoom many to one
    @ManyToOne
    @JoinColumn(name = "chatRoom", nullable = false, referencedColumnName = "id")
    private ChatRoom chatRoom;

}

