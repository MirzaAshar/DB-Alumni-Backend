package com.alumni.blog.services.implementation;

import com.alumni.blog.entities.Category;
import com.alumni.blog.entities.Post;
import com.alumni.blog.entities.User;
import com.alumni.blog.exceptions.ResourceNotFoundException;
import com.alumni.blog.payloads.CommentDto;
import com.alumni.blog.payloads.PostDto;
import com.alumni.blog.payloads.PostResponse;
import com.alumni.blog.repository.CategoryRepo;
import com.alumni.blog.repository.PostRepo;
import com.alumni.blog.repository.UserRepo;
import com.alumni.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImplementation implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public PostDto createPost(PostDto postDto, Integer userID, Integer categoryId, MultipartFile image) {
        User user = this.userRepo.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userID));
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Post post = this.modelMapper.map(postDto, Post.class);
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(cat);

        // Define the image directory path
        String imageDirectory = "E:\\Blog-Api\\Backend-images";

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            // Generate a unique image name
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String imagePath = imageDirectory + File.separator + imageName;

            // Ensure the directory exists
            File directory = new File(imageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the directory
            try {
                // Save the file
                Files.copy(image.getInputStream(), Paths.get(imagePath));
                post.setImageName(imageName);
                post.setImageData(image.getBytes());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save the image file: " + ex.getMessage(), ex);
            }
            post.setImageName(imageName);
        } else {
            post.setImageName("default.png"); // Use a default image if no file is uploaded
        }

        Post newPost = this.postRepo.save(post);
        return this.modelMapper.map(newPost, PostDto.class);
    }

   @Override
    public PostDto updatePost(PostDto postDto, Integer postId, Integer categoryId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
        post.setCategory(cat);
        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->  new ResourceNotFoundException("Post", "postId", postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Post> pagePost = this.postRepo.findAll(p);
        List<Post> allPosts = pagePost.getContent();

        List<PostDto> postDto = allPosts.stream()
                .map(post -> {
                    PostDto dto = this.modelMapper.map(post, PostDto.class);
                    String imageUrl = "http://localhost:8080/post/image/" + post.getImageName();
                    dto.setImageName(imageUrl);

                    // Fetch the image bytes and set in the PostDto
                    String imagePath = "C:\\Users\\sayal\\OneDrive\\Desktop\\Blog-Api\\images" + File.separator + post.getImageName();
                    File imageFile = new File(imagePath);

                    if (imageFile.exists()) {
                        try {
                            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                            dto.setImageData(imageBytes);
                        } catch (IOException e) {
                            e.printStackTrace(); // Log the error
                        }
                    }
                    return dto;
                }).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDto);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements((int) pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }


    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        PostDto postDto = this.modelMapper.map(post, PostDto.class);

        // Map each comment to include userName
        postDto.setComments(
                post.getComments().stream().map(comment -> {
                    CommentDto commentDto = this.modelMapper.map(comment, CommentDto.class);
                    commentDto.setUserName(comment.getUserName());
                    return commentDto;
                }).collect(Collectors.toSet())
        );

        // Add the image URL
        String imageUrl = "http://localhost:8080/post/image/" + post.getImageName();
        postDto.setImageName(imageUrl);

        // Fetch the image bytes and set in the PostDto
        String imagePath = "C:\\Users\\sayal\\OneDrive\\Desktop\\Blog-Api\\images" + File.separator + post.getImageName();
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            try {
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                postDto.setImageData(imageBytes);
            } catch (IOException e) {
                e.printStackTrace(); // Log the error
            }
        }

        return postDto;
    }


@Override
public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize) {
    Pageable p = PageRequest.of(pageNumber, pageSize);

    // Fetch the user or throw an exception if not found
    User user = this.userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

    // Fetch paginated posts for the given user
    Page<Post> pagePost =  this.postRepo.findByUser(user, p);

    // Convert Post entities to PostDto
    List<PostDto> postDto = pagePost.getContent().stream()
            .map(post -> this.modelMapper.map(post, PostDto.class))
            .toList();

    // Build the PostResponse
    PostResponse postResponse = new PostResponse();
    postResponse.setContent(postDto);
    postResponse.setPageNumber(pagePost.getNumber());
    postResponse.setPageSize(pagePost.getSize());
    postResponse.setTotalPages(pagePost.getTotalPages());
    postResponse.setTotalElements((int) pagePost.getTotalElements());
    postResponse.setLastPage(pagePost.isLast());

    return postResponse;
}


    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
        Page<Post> pagePost =  this.postRepo.findByCategory(cat, p);
        List<PostDto> postDto = pagePost.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .toList();
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDto);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements((int) pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());
        return  postResponse;
    }


    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepo.searchByKeyword(keyword);
        return posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }
}
