package com.spot.fun.usr.signup.service;
import com.spot.fun.usr.user.dto.UserDTO;

public interface SignupService {
  void signup(UserDTO userDTO);
  void socialSignup(UserDTO userDTO);
}
