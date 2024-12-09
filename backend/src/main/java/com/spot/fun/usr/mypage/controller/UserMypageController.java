package com.spot.fun.usr.mypage.controller;

import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import com.spot.fun.usr.mypage.service.UserMypageService;
import com.spot.fun.usr.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/mypage")
public class UserMypageController {
    private final UserMypageService userMypageService;
    private final UserFeedService userFeedService;
    private final AuthTokenUtil authTokenUtil;

    @GetMapping("/info")
    public UserDTO info() {
        return userMypageService.findByIdx();
    }

    @GetMapping("/feed")
    public ResponseEntity<?> feedList(HttpServletRequest request, HttpServletResponse response,
                                      FeedRequestDTO feedRequestDTO) {
        UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
        if (!Objects.isNull(loginUserDTO.getIdx())) {
            feedRequestDTO.setLoginUserDTO(loginUserDTO);
        } else { // 비로그인 상태는 접근불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        userFeedService.getListByMypage(feedRequestDTO);
        return null;
    }
}
