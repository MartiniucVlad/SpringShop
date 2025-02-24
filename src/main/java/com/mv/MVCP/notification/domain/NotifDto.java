package com.mv.MVCP.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NotifDto {

    private Long id;


    private Long sender_id;

    private String sender_name;

    private Long recipient_id;

    private String type;

    private String message;

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
