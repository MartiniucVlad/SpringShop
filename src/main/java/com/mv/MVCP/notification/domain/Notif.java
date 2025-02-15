package com.mv.MVCP.notification.domain;

import com.mv.MVCP.models.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = true)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    private UserEntity recipient;

    @Column(nullable = false)
    private NotifType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
}
