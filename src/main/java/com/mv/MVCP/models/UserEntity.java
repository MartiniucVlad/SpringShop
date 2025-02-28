package com.mv.MVCP.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String email;
    private String password;
    private String statusType = "online";


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles = new ArrayList<>();


    // only friends with a greater id are stored, needs querying the user_id2 too for complete friend list
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id1", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id2", referencedColumnName = "id")
    )
    private List<UserEntity> partialFriendList = new ArrayList<>();

    public void addRole(RoleEntity role) {
        roles.add(role);
    }
}
