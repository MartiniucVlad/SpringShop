package com.mv.MVCP.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDisplayDto {
    @NotEmpty
    private long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String statusType;
}
