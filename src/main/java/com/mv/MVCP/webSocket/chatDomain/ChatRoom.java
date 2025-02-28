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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;


    @ManyToOne()
    @JoinColumn(name = "user_1", referencedColumnName = "id", nullable = true)
    private UserEntity user1;

    @ManyToOne()
    @JoinColumn(name = "user_2", referencedColumnName = "id", nullable = true)
    private UserEntity user2;

    @Column(nullable = true)
    Integer nrUnreadUser1;

    @Column(nullable = true)
    Integer nrUnreadUser2;

    @PrePersist
    @PreUpdate
    private void validateUsers() {
        if (type == ChatRoomType.PRIVATE) {
            if (user1 == null || user2 == null) {
                throw new IllegalStateException("Private rooms must have user1 and user2 defined");
            }
            if (user1.getId() > user2.getId()) {
                throw new IllegalStateException("User1's ID must be less than User2's ID");
            }
        }
    }

}
