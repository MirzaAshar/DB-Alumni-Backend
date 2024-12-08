package com.alumni.blog.security;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    private int userID;

}