package com.alumni.blog.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> content;
    private int pageSize;
    private int totalElements;
    private int totalPages;
    private boolean lastPage;
    private int pageNumber;
}
