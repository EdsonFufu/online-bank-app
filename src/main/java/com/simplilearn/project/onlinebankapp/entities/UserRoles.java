package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_ROLES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoles {

    @Id
    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "ROLE_ID")
    private int roleId;
}
