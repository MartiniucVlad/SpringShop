package com.mv.MVCP.controller;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.UserEntity;
import com.mv.MVCP.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserEntity getUser () {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            return user;
        }
        return null;
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute RegistrationDto registrationDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        UserEntity existingUserEmail = userService. findByEmail(registrationDto.getEmail());
        if(existingUserEmail != null) {
            return "redirect:/register?takenEmail";
        }
        UserEntity existingUserUsername = userService.findByUsername(registrationDto.getUsername());
        if(existingUserUsername != null) {
            return "redirect:/register?takenUser";
        }

        userService.saveUser(registrationDto);
        return "redirect:/products?success";
    }

    @PostMapping("/status/{type}")
    public ResponseEntity<String> changeStatus(@PathVariable String type) {
        UserEntity user = getUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        if ("online".equals(type) || "invisible".equals(type)) {
           userService.changeStatus(user, type);
            return ResponseEntity.ok("Status updated to " + type);
        }

        return ResponseEntity.badRequest().body("Invalid status type");
    }



    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }
}
