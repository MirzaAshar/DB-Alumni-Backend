package com.alumni.blog.controllers;
import com.alumni.blog.payloads.ApiResponse;
import com.alumni.blog.payloads.UserDto;
import com.alumni.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userID)
    {
       UserDto updatedUser= this.userService.updateUser(userDto, userID);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userID) {
        this.userService.deleteUser(userID);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return  ResponseEntity.ok(this.userService.getAllUsers());
    }
    @GetMapping("/{userID}")
    public ResponseEntity<UserDto> getSingleUsers(@PathVariable Integer userID)
    {
        return  ResponseEntity.ok(this.userService.getUserByID(userID));
    }


}
