package com.spot.fun.usr.oauthlogin.service;

import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> originalAttributes = oAuth2User.getAttributes();

    // 새로운 Map을 생성하여 attributes 수정 가능하도록 처리
    Map<String, Object> attributes = new HashMap<>(originalAttributes);

    // 사용자 정보 추출
    String email = extractEmailFromProvider(registrationId, attributes);
    String name = extractNameFromProvider(registrationId, attributes);
    String nickname = extractNicknameFromProvider(registrationId, attributes);

    if (email == null) {
      log.error("OAuth2 provider did not return an email address for registrationId: {}", registrationId);
      throw new OAuth2AuthenticationException("OAuth2 provider did not return an email address.");
    }

    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      log.info("Existing user found: {}", user.getEmail());

      // 기존 사용자 속성 추가
      attributes.put("email", email);
      attributes.put("name", name);
      attributes.put("nickname", nickname);

      return new DefaultOAuth2User(
              Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getRole())),
              attributes,
              "email"
      );
    }

    // 비회원 처리: 사용자 정보를 세션에 저장
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    log.info("OAuth Attributes: {}", attributes);
    log.info("Registration ID: {}", registrationId);
    log.info("Email: {}", email);
    log.info("Name: {}", name);
    log.info("Nickname: {}", nickname);

    request.getSession().setAttribute("oauthAttributes", attributes);
    request.getSession().setAttribute("registrationId", registrationId);
    request.getSession().setAttribute("email", email);
    request.getSession().setAttribute("name", name);
    request.getSession().setAttribute("nickname", nickname);

    attributes.put("email", email); // 비회원도 email 속성 추가
    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST")),
            attributes,
            "email"
    );
  }


  private String extractEmailFromProvider(String registrationId, Map<String, Object> attributes) {
    log.debug("Extracting email for registrationId: {}", registrationId);
    if ("google".equals(registrationId)) {
      return (String) attributes.get("email");
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      return response != null ? (String) response.get("email") : null;
    }
    return null;
  }

  private String extractNameFromProvider(String registrationId, Map<String, Object> attributes) {
    log.debug("Extracting name for registrationId: {}", registrationId);
    if ("google".equals(registrationId)) {
      return (String) attributes.get("name");
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
      return profile != null ? (String) profile.get("nickname") : null;
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      return response != null ? (String) response.get("name") : null;
    }
    return null;
  }

  private String extractNicknameFromProvider(String registrationId, Map<String, Object> attributes) {
    log.debug("Extracting nickname for registrationId: {}", registrationId);
    if ("google".equals(registrationId)) {
      return (String) attributes.get("name"); // Google에서는 이름이 닉네임 역할을 함
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
      return profile != null ? (String) profile.get("nickname") : null;
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      return response != null ? (String) response.get("nickname") : null;
    }
    return null;
  }
}
