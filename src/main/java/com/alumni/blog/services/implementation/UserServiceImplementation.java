package com.alumni.blog.services.implementation;

import com.alumni.blog.entities.Role;
import com.alumni.blog.entities.User;
import com.alumni.blog.exceptions.ResourceNotFoundException;
import com.alumni.blog.payloads.UserDto;
import com.alumni.blog.repository.UserRepo;
import com.alumni.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDto createUser(UserDto userDto) {
       User user= this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser=this.userRepo.save(user);
       return this.userToDto(savedUser);
    }
//    private User dtoToUser(UserDto userDto) {
//        User user =
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setAbout(userDto.getAbout());
//        return user;
//    }
    public UserDto userToDto(User user) {
       return this.modelMapper.map(user, UserDto.class);

    }
    @Override
    public UserDto updateUser(UserDto userDto, Integer userID) {
       User user=this.userRepo.findById(userID).orElseThrow(()->new ResourceNotFoundException("User", "Id", userID));
       user.setName(userDto.getName());
       user.setEmail(userDto.getEmail());
       user.setPassword(userDto.getPassword());
       user.setAbout(userDto.getAbout());
       user.setCnic(userDto.getCnic());
       user.setUniversityID(userDto.getUniversityID());
       user.setCurrentOrganization(userDto.getCurrentOrganization());
       user.setCurrentDesignation(userDto.getCurrentDesignation());
       user.setCurrentCountry(userDto.getCurrentCountry());
       user.setCurrentCity(userDto.getCurrentCity());
       user.setCampusLocation(userDto.getCampusLocation());
       user.setGraduationYear(userDto.getGraduationYear());
       user.setDegreeProgram(userDto.getDegreeProgram());
       user.setMajor(userDto.getMajor());
       user.setConfirmpassword(userDto.getConfirmpassword());
       User updatedUser=this.userRepo.save(user);

       return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserByID(Integer userID) {
       User user=this.userRepo.findById(userID).orElseThrow(()->new ResourceNotFoundException("User", "Id", userID));
       UserDto userDto=userToDto(user);
       userDto.setPassword(null);
       return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=this.userRepo.findAll();
        List<UserDto> userDtos= users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userID) {
   User user= this.userRepo.findById(userID).orElseThrow(()->new ResourceNotFoundException("User", "Id", userID));
    this.userRepo.delete(user);
    }
    @Override
    public UserDto registerUser(UserDto userDto) {
        // Check if the email already exists
        if (this.userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + userDto.getEmail() + " already exists.");
        }

        // Convert UserDto to User entity
        User user = this.modelMapper.map(userDto, User.class);
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmpassword(passwordEncoder.encode(user.getConfirmpassword()));
        // Assign default role to the user
        Role defaultRole = new Role();
        defaultRole.setId(1); // Ensure the default role ID matches your database
        defaultRole.setName("ROLE_USER"); // Default role name
        user.getRoles().add(defaultRole);

        // Save the new user in the database
        User savedUser = this.userRepo.save(user);

        // Convert saved User back to UserDto
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto makeAdmin(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Role adminRole = new Role();
        adminRole.setId(2); // Ensure this matches the ID of the "ROLE_ADMIN" in your database
        adminRole.setName("ROLE_ADMIN");

        user.getRoles().add(adminRole); // Add the admin role
        User updatedUser = this.userRepo.save(user); // Save the user with the new role

        return this.userToDto(updatedUser);
    }

}
