package com.alumni.blog.controllers;

import com.alumni.blog.payloads.ApiResponse;
import com.alumni.blog.payloads.CommentDto;
import com.alumni.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/post/{postId}/user/{userId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment, @PathVariable Integer postId, @PathVariable Integer userId) {
    CommentDto createdComment= this.commentService.createComment(comment, postId, userId);
    return new ResponseEntity<CommentDto>(createdComment, HttpStatus.OK);
    }
    @DeleteMapping("/commentId")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
       this.commentService.deleteComment(commentId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted Sucessfully", true), HttpStatus.OK);
    }


}
