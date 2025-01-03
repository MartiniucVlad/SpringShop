package com.mv.MVCP.controller;

import com.mv.MVCP.Service.UserService;
import com.mv.MVCP.dto.RegistrationDto;
import com.mv.MVCP.models.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

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
        UserEntity existingUserUsername = userService.findByUser(registrationDto.getUsername());
        if(existingUserUsername != null) {
            return "redirect:/register?takenUser";
        }

        userService.saveUser(registrationDto);
        return "redirect:/products?success";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }
}
