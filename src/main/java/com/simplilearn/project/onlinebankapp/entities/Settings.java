package com.simplilearn.project.onlinebankapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private String companyName;
    private String address;
    private String withdrawFee;
}
