package com.spot.fun.usr.user.controller;

import com.spot.fun.file.FileUploadUtil;
import com.spot.fun.usr.user.dto.profile.UserProfileRequestDTO;
import com.spot.fun.usr.user.dto.profile.UserProfileResponseDTO;
import com.spot.fun.usr.user.service.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/usr/profile")
public class UserProfileController {

  private final UserProfileService userProfileService;
  private final FileUploadUtil fileUploadUtil;

  @GetMapping("")
  public ResponseEntity<?> getProfile(UserProfileRequestDTO userProfileRequestDTO) {
    UserProfileResponseDTO profile = userProfileService.getProfile(userProfileRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(profile);
  }

  @GetMapping("/image/{fileName}")
  public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) {
    String menuType = "profile";
    return fileUploadUtil.outputImage(menuType, fileName);
  }
}
