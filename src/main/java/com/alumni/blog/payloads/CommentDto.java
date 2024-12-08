package com.alumni.blog.payloads;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CommentDto {
    private Integer id;
    private String content;
    private String userName;
}
