package com.mv.MVCP.notification.ServiceNotification;

import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.notification.domain.Notif;
import com.mv.MVCP.notification.domain.NotifDto;

import java.util.List;


public interface NotificationService {


    void saveNotif(Notif notif);

    void deleteNotif(Long id);

    List<NotifDto> getNotifOfUser(UserEntity user);

}
