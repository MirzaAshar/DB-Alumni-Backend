package com.alumni.blog.controllers;
import com.alumni.blog.payloads.ApiResponse;
import com.alumni.blog.payloads.CategoryDto;
import com.alumni.blog.payloads.PostDto;
import com.alumni.blog.payloads.PostResponse;
import com.alumni.blog.services.FileService;
import com.alumni.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;


@RestController
@RequestMapping("/api/")
public class PostController {
    //create
    @Autowired
    private FileService fileService;
    @Autowired
    private PostService postService;
    @Value("project.image")
    private String path;
//    @PostMapping("user/{userId}/category/{categoryId}/posts")
//    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto
//    , @PathVariable Integer userId, @PathVariable Integer categoryId) {
//    PostDto createPost=this.postService.createPost(postDto, userId, categoryId);
//        return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
//    }
    @PostMapping("user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestPart("postDto") PostDto postDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) throws IOException {
        PostDto createPost = this.postService.createPost(postDto, userId, categoryId, image);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

    //get by user- implement pagination
    @GetMapping({"/user/{userId}/posts"})
    public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId, @RequestParam(value="pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value="pageSize", defaultValue = "10", required = false)Integer pageSize) {
        PostResponse posts=this.postService.getPostsByUser(userId, pageNumber, pageSize);
        return new ResponseEntity<PostResponse>(posts, HttpStatus.OK);
    }
    //- implement pagination
    @GetMapping({"/category/{categoryId}/posts"})
    public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,@RequestParam(value="pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value="pageSize", defaultValue = "10", required = false)Integer pageSize) {
        PostResponse posts=this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
        return new ResponseEntity<PostResponse>(posts, HttpStatus.OK);
    }
    @PutMapping("/posts/{postId}/{categoryId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId, @PathVariable Integer categoryId) {
        PostDto updatedPost = this.postService.updatePost(postDto, postId, categoryId);
        return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
    }
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId) {
        this.postService.deletePost(postId);
        return new ApiResponse("Post has been deleted sucessfully", true);
    }
    // Controller for fetching all posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy) {

        PostResponse posts = this.postService.getAllPost(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Controller for fetching a post by its ID
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto post = this.postService.getPostById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/posts/count")
    public ResponseEntity<Long> countAllPosts() {
        long count = this.postService.countAllPosts();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping("/posts/search/{search}")
    public ResponseEntity<List<PostDto>> searchPosts(@PathVariable String search) {
        List<PostDto> posts = this.postService.searchPosts(search);
        return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
    }
    //post Image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId) throws IOException {
        String fileName=this.fileService.uploadImage(path,image);
        PostDto postDto=this.postService.getPostById(postId);
        postDto.setImageName(fileName);
        CategoryDto cat=postDto.getCategory();
        PostDto updatePost=this.postService.updatePost(postDto, postId, cat.getCategoryId());
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
    }
    @GetMapping(value="/post/image/{imageName}", produces= MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException{
        InputStream resource=this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
    @GetMapping("/post/image/{imageName}")
    public ResponseEntity<?> getPostImage(@PathVariable String imageName) {
        String imagePath = "E:\\Blog-Api-Final\\images" + File.separator + imageName;
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            try {
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Or dynamic content type if necessary
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Error reading image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
