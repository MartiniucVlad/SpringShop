package com.mv.MVCP.controller;

import com.mv.MVCP.Service.PostService;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.PostEntityDto;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    public UserEntity getCurrUser () {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            return user;
        }
        return null;
    }


    @GetMapping("/posts")
    public String posts(Model model) {
        List<PostEntityDto> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post-list";
    }

    @GetMapping("/posts/{id}")
    public String postDetails(@PathVariable("id") Long id, Model model) {
        PostEntityDto postEntityDto = postService.findById(id);
        if (postEntityDto == null) {
            return "redirect:/posts";
        }
        model.addAttribute("post", postEntityDto);
        return "post-details";
    }

    @GetMapping("/posts/create")
    public String createPostForm(Model model) {
        PostEntityDto postEntityDto = new PostEntityDto();
        model.addAttribute("postDto", postEntityDto);
        return "post-create";
    }

    @PostMapping("/posts/create")
    public String createPost(@Valid @ModelAttribute("postDto") PostEntityDto postEntityDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post-create";
        }
        postService.insertPost(postEntityDto);
        return "redirect:/posts";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts/edit/{id}")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        PostEntityDto postEntityDto = postService.findById(id);
        if (postEntityDto == null) {
            return "redirect:/posts";
        }
        model.addAttribute("postDto", postEntityDto);
        model.addAttribute("postId", id);
        return "post-edit";
    }

    @PostMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id,
                           @Valid @ModelAttribute("postDto") PostEntityDto postEntityDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post-edit";
        }
        postService.insertPost(postEntityDto);
        return "redirect:/posts";
    }

    @GetMapping("/posts/search")
    public String searchPosts(@RequestParam String query, Model model) {
        List<PostEntityDto> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        return "post-list";
    }



}