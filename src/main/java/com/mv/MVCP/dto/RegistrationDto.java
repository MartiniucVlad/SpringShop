package com.mv.MVCP.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationDto {

    private long id;
    @NotEmpty
    private String username;
    @Email
    private String email;
    @NotEmpty
    private String password;



//   public @NotEmpty String getUsername() {
//       return username;
//   }
//    public @Email String getEmail() {
//        return email;
//    }

}
