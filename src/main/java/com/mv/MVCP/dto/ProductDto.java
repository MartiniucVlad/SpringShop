package com.mv.MVCP.dto;

import com.mv.MVCP.models.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
public class ProductDto {

    private Long  id;

    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Photo URL is required")
    private String photoUrl;
    @NotEmpty(message = "Content is required")
    private String content;
    private UserEntity createdBy;





    public ProductDto() {};


    @Override
    public String toString() {
        return "ProductDto{" +
                ", title='" + title + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
