package com.spot.fun.usr.user.service;

import com.spot.fun.usr.user.dto.UserDTO;

public interface UserService {
    UserDTO findByIdx(Long idx);
    UserDTO findByUserId(String userId);
    String findUserIdByDetails(String name, String birthDate, String email);
    void updatePassword(String userId, String email, String newPassword);
}
