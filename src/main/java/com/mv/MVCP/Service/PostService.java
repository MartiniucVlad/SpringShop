package com.mv.MVCP.Service;

import com.mv.MVCP.dto.PostEntityDto;
import com.mv.MVCP.models.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    List<PostEntityDto> findAll();

    void insertPost(PostEntityDto postEntityDto);

    List<PostEntityDto> findPostsByUser(UserEntity user);

    PostEntityDto findById(Long id);

    void editPost(PostEntityDto postEntityDto);

    void deletePost(Long id);

    List<PostEntityDto> searchPosts(String title);
}
