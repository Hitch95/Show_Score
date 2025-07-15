package com.example.show_score.dto;

import com.example.show_score.model.UserBean;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserBean user;
}