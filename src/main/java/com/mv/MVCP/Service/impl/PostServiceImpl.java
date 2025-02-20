package com.mv.MVCP.Service.impl;

import com.mv.MVCP.Service.PostService;
import com.mv.MVCP.dto.PostEntityDto;
import com.mv.MVCP.models.PostEntity;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.repository.PostRepository;
import com.mv.MVCP.repository.UserRepository;
import com.mv.MVCP.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PostEntityDto> findAll() {
        List<PostEntity> postEntities = postRepository.findAll();
        return postEntities.stream().map(this::toProductDto).toList();
    }

    @Override
    public void insertPost(PostEntityDto postEntityDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        postEntityDto.setCreatedBy(user);
        postRepository.save(toProduct(postEntityDto));
    }

    @Override
    public PostEntityDto findById(Long id) {
         Optional<PostEntity> product = postRepository.findById(id);
         if(product.isPresent()) {
             return toProductDto(product.get());
         }
         else {
             return null;
         }
    }

    @Override
    public void editPost(PostEntityDto postEntityDto) {

    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<PostEntityDto> searchPosts(String title) {
        List<PostEntity> postEntities = postRepository.searchProducts(title);
        return postEntities.stream().map(this::toProductDto).toList();
    }


    private PostEntityDto toProductDto(PostEntity postEntity) {
        PostEntityDto postEntityDto = PostEntityDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .photoUrl(postEntity.getPhotoUrl())
                .content(postEntity.getContent())
                .createdBy(postEntity.getCreatedBy())
                .build();

        return postEntityDto;
    }

    private PostEntity toProduct(PostEntityDto postEntityDto) {
        PostEntity postEntity = PostEntity.builder()
                .title(postEntityDto.getTitle())
                .photoUrl(postEntityDto.getPhotoUrl())
                .content(postEntityDto.getContent())
                .createdBy(postEntityDto.getCreatedBy())
                .build();
        return postEntity;
    }


}
