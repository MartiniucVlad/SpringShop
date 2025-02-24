package com.mv.MVCP.controller;

import com.google.gson.Gson;
import com.mv.MVCP.Service.PostService;
import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.PostEntityDto;
import com.mv.MVCP.dto.UserDisplayDto;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    public UserEntity getCurrUser () {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            return user;
        }
        return null;
    }

    @GetMapping("/profile/{id}")
    public String displayProfile(@PathVariable("id") Long id, Model model) {
        UserEntity displayUser = userService.findById(id);
        if (displayUser == null) {
            return "redirect:/posts";
        }
        List<PostEntityDto> posts = postService.findPostsByUser(displayUser);
        model.addAttribute("posts", posts);
        model.addAttribute("displayUser", displayUser);
        boolean areFriends = userService.areFriends(getCurrUser(), displayUser);
        model.addAttribute("areFriends", areFriends);

        return "profile-display";
    }

    List<UserDisplayDto> getFriendList(UserEntity user) {
        List<UserEntity> friends = userService.getFriendList(user);
        return friends.stream().map(userService::toUserDisplayDto).toList();
    }


    @GetMapping("friend-list")
    public String friendList(Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity currUser = userService.findByUsername(username);
        model.addAttribute("friendList", getFriendList(currUser));
        return "friend-list";
    }

    @PostMapping("/remove-friend")
    public ResponseEntity<String> removeFriend(@RequestBody String friendIdString) {
        Long friendId = Long.valueOf(friendIdString);
        System.out.print("Removing friend with ID: " + friendId);
        userService.removeFriend(getCurrUser().getId(), friendId);
        return ResponseEntity.ok("Friend removed");
    }


}
