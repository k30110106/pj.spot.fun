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
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    // 사용자 이메일 추출
    String email = extractEmailFromProvider(registrationId, attributes);
    if (email == null) {
      throw new OAuth2AuthenticationException("OAuth2 provider did not return an email address.");
    }

    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
      // 기존 사용자 처리
      User user = userOptional.get();
      return new DefaultOAuth2User(
              Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getRole())),
              attributes,
              userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
      );
    }

    // 비회원 처리: 사용자 정보를 세션에 저장
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    request.getSession().setAttribute("oauthAttributes", attributes);
    request.getSession().setAttribute("registrationId", registrationId);

    throw new OAuth2AuthenticationException("Redirecting to signup page");
  }

  private String extractEmailFromProvider(String registrationId, Map<String, Object> attributes) {
    if ("google".equals(registrationId)) {
      return (String) attributes.get("email");
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      return (String) kakaoAccount.get("email");
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      return (String) response.get("email");
    }
    return null;
  }
}
