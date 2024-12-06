package com.alumni.blog.services;

import com.alumni.blog.payloads.UserDto;

import java.util.List;

public interface UserService {
   UserDto createUser(UserDto user);
   UserDto updateUser(UserDto user, Integer userID);
   UserDto getUserByID(Integer userID);
   List<UserDto> getAllUsers();
   void deleteUser(Integer userID);
   UserDto registerUser(UserDto userDto);
   public UserDto makeAdmin(Integer userId);
}
