package com.spot.fun.usr.mypage.service;

import com.spot.fun.usr.user.dto.UserDTO;

public interface UserMypageService {
  UserDTO findByIdx();

  UserDTO getUser(UserDTO userDTO);
}
