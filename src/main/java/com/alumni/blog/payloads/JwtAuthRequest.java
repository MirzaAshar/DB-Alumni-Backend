package com.alumni.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class JwtAuthRequest {
    private String username;
  //  @JsonIgnore
    private String password;
}
