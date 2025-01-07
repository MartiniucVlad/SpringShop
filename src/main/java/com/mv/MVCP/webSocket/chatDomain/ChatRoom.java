package com.mv.MVCP.webSocket.chatDomain;

import com.mv.MVCP.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "chatRoom")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    @OneToOne()
    @JoinColumn(name = "user_1", referencedColumnName = "id")

    private UserEntity user1;

    @OneToOne()
    @JoinColumn(name = "user_2", referencedColumnName = "id")
    private UserEntity user2;

    @PrePersist
    @PreUpdate
    private void validateUsers() {
        if (user1.getId() > user2.getId()) {
            throw new IllegalStateException("User1's ID must be less than User2's ID");
        }
    }

}
