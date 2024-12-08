package com.alumni.blog.controllers;
import com.alumni.blog.entities.User;
import com.alumni.blog.payloads.JwtAuthRequest;
import com.alumni.blog.payloads.UserDto;
import com.alumni.blog.repository.UserRepo;
import com.alumni.blog.security.CustomUserDetails;
import com.alumni.blog.security.JwtAuthResponse;
import com.alumni.blog.security.JwtTokenHelper;
import com.alumni.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {
        if (request.getUsername() == null || request.getPassword() == null ||
                request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password must not be empty");
        }

        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails= this.userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepo.findByEmail(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
          String token=  this.jwtTokenHelper.generateToken(userDetails);
          JwtAuthResponse response=new JwtAuthResponse();
        response.setUserID(user.getId());
          response.setToken(token);

          return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }
    public void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            // Attempt authentication
            this.authenticationManager.authenticate(authToken);
            System.out.println("Authentication successful for user: " + username); // Debugging log
        } catch (DisabledException e) {
            // User is disabled
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is disabled");
        } catch (BadCredentialsException e) {
            // Invalid username or password
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (AuthenticationException e) {
            // Any other authentication exception
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed: " + e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            UserDto registeredUser = this.userService.registerUser(userDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/make-admin/{userId}")
    public ResponseEntity<UserDto> makeAdmin(@PathVariable Integer userId) {
        UserDto updatedUser = this.userService.makeAdmin(userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}