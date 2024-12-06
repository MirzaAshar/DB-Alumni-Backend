package com.alumni.blog.services.implementation;
import com.alumni.blog.entities.Comment;
import com.alumni.blog.entities.Post;
import com.alumni.blog.entities.User;
import com.alumni.blog.exceptions.ResourceNotFoundException;
import com.alumni.blog.payloads.CommentDto;
import com.alumni.blog.repository.CommentRepo;
import com.alumni.blog.repository.PostRepo;
import com.alumni.blog.repository.UserRepo;
import com.alumni.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImplementation implements CommentService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ModelMapper modelMapper;
//    @Override
//    public CommentDto createComment(CommentDto commentDto, Integer postId) {
//        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "postId", postId));
//        Comment comment= this.modelMapper.map(commentDto, Comment.class);
//        comment.setPost(post);
//        Comment savedComment=this.commentRepo.save(comment);
//
//        return this.modelMapper.map(savedComment, CommentDto.class);
//    }
@Autowired
private UserRepo userRepo; // Repository for User entity

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);


        comment.setUserName(user.getName());
        Comment savedComment = this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);
    }


    @Override
    public void deleteComment(Integer commentId) {
    Comment com=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "commentId", commentId));
    this.commentRepo.delete(com);
    }
}
