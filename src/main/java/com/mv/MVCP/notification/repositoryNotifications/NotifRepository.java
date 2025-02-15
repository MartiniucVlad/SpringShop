package com.mv.MVCP.notification.repositoryNotifications;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.notification.domain.Notif;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotifRepository extends JpaRepository<Notif, Long> {
    Notif findById(long id);
    List<Notif> findAllByRecipient(UserEntity recipient);
}
