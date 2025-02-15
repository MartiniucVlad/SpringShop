package com.mv.MVCP.notification.domain;

import lombok.Builder;

@Builder
public class NotifDto {

    private Long sender_id;

    private String sender_name;

    private Long recipient_id;

    String type;

    String message;

    @Override
    public String toString() {
        return "NotifDto{" +
                "sender_id=" + sender_id +
                ", sender_name='" + sender_name + '\'' +
                ", recipient_id=" + recipient_id +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
