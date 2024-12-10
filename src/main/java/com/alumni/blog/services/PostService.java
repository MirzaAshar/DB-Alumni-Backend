package com.alumni.blog.services;

import com.alumni.blog.entities.Category;
import com.alumni.blog.entities.Post;
import com.alumni.blog.payloads.PostDto;
import com.alumni.blog.payloads.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService  {
        PostDto createPost(PostDto postDto, Integer userId, Integer categoryId, MultipartFile image);

        PostDto updatePost(PostDto postDto, Integer postId, Integer categoryId);

        void deletePost(Integer postId);

        PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy);

        PostDto getPostById(Integer postId);

        PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize );

        PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize);

        List<PostDto> searchPosts(String keyword);

        long countAllPosts();

}
