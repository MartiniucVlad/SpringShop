package com.mv.MVCP.dto;

import com.mv.MVCP.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class PostEntityDto {

    Long id;

    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Photo URL is required")
    private String photoUrl;
    @NotEmpty(message = "Content is required")
    private String content;

    private UserEntity createdBy;


    @Override
    public String toString() {
        return "ProductDto{" +
                ", title='" + title + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
