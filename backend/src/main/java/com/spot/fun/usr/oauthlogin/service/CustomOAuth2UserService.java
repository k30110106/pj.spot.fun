package com.spot.fun.usr.oauthlogin.service;

import com.spot.fun.usr.oauthlogin.converter.KakaoRequestEntityConverter;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
    log.info("Client Registration: {}", userRequest.getClientRegistration());
    log.info("Access Token: {}", userRequest.getAccessToken().getTokenValue());

    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    // 카카오 전용 처리
//    if ("kakao".equals(registrationId)) {
//      try {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
//
//        String tokenUri = userRequest.getClientRegistration().getProviderDetails().getTokenUri();
//        log.info("Kakao Token URI: {}", tokenUri);
//
//        // Authorization Code는 userRequest의 추가 파라미터에서 가져옵니다.
//        String authorizationCode = (String) userRequest.getAdditionalParameters().get("code");
//        if (authorizationCode == null) {
//          throw new OAuth2AuthenticationException(new OAuth2Error("invalid_request", "Authorization code is missing.", null));
//        }
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
//        body.add(OAuth2ParameterNames.CODE, authorizationCode); // 수정된 부분
//        body.add(OAuth2ParameterNames.REDIRECT_URI, userRequest.getClientRegistration().getRedirectUri());
//        body.add(OAuth2ParameterNames.CLIENT_ID, userRequest.getClientRegistration().getClientId());
//
//        if (userRequest.getClientRegistration().getClientSecret() != null) {
//          body.add(OAuth2ParameterNames.CLIENT_SECRET, userRequest.getClientRegistration().getClientSecret());
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(tokenUri));
//        log.info("Request Entity: {}", requestEntity);
//
//        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
//        log.info("Token Response: {}", response);
//
//        Map<String, Object> tokenResponse = response.getBody();
//
//        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
//          log.error("Invalid Token Response: {}", tokenResponse);
//          throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token_response", "Kakao response is null or invalid", null));
//        }
//
//        // Access Token 추출
//        String accessToken = (String) tokenResponse.get("access_token");
//        log.info("Kakao Access Token: {}", accessToken);
//
//        // 사용자 정보 요청
//        Map<String, Object> userInfo = fetchKakaoUserInfo(accessToken);
//        log.info("Kakao User Info: {}", userInfo);
//
//        return createOAuth2User(userRequest, userInfo, "email");
//
//      } catch (Exception e) {
//        log.error("Failed to handle Kakao OAuth2 token exchange", e);
//        throw e;
//      }
//    }

//    // 기본 처리 (Google, Naver 등)
//    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//    OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//    String registrationIdFromAttributes = userRequest.getClientRegistration().getRegistrationId();
//    Map<String, Object> originalAttributes = oAuth2User.getAttributes();
//
//    // 새로운 Map을 생성하여 attributes 수정 가능하도록 처리
//    Map<String, Object> attributes = new HashMap<>(originalAttributes);
//
//    // 사용자 정보 추출
//    String email = extractEmailFromProvider(registrationIdFromAttributes, attributes);
//    String name = extractNameFromProvider(registrationIdFromAttributes, attributes);
//    String nickname = extractNicknameFromProvider(registrationIdFromAttributes, attributes);
//
//    log.info("OAuth2 provider: {}", registrationIdFromAttributes);
//    log.info("OAuth2 attributes: {}", oAuth2User.getAttributes());
//
//    Optional<User> userOptional = userRepository.findByEmail(email);
//    if (userOptional.isPresent()) {
//      User user = userOptional.get();
//      log.info("Existing user found: {}", user.getEmail());
//
//      // 기존 사용자 속성 추가
//      attributes.put("email", email);
//      attributes.put("name", name);
//      attributes.put("nickname", nickname);
//
//      return new DefaultOAuth2User(
//              Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getRole())),
//              attributes,
//              "email"
//      );
//    }

    // Kakao 전용 처리
    if ("kakao".equals(registrationId)) {
      try {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // Authorization Code 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationCode = request.getParameter("code");
        if (authorizationCode == null) {
          throw new OAuth2AuthenticationException(new OAuth2Error("invalid_request", "Authorization code is missing.", null));
        }

        String tokenUri = userRequest.getClientRegistration().getProviderDetails().getTokenUri();
        log.info("Kakao Token URI: {}", tokenUri);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
        body.add(OAuth2ParameterNames.REDIRECT_URI, userRequest.getClientRegistration().getRedirectUri());
        body.add(OAuth2ParameterNames.CLIENT_ID, userRequest.getClientRegistration().getClientId());
        body.add("code", authorizationCode); // Authorization Code 직접 전달

        if (userRequest.getClientRegistration().getClientSecret() != null) {
          body.add(OAuth2ParameterNames.CLIENT_SECRET, userRequest.getClientRegistration().getClientSecret());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(tokenUri));
        log.info("Request Entity: {}", requestEntity);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        log.info("Token Response: {}", response);

        Map<String, Object> tokenResponse = response.getBody();
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
          log.error("Invalid Token Response: {}", tokenResponse);
          throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token_response", "Kakao response is null or invalid", null));
        }

        // Access Token 추출
        String accessToken = (String) tokenResponse.get("access_token");
        log.info("Kakao Access Token: {}", accessToken);

        // 사용자 정보 요청
        Map<String, Object> userInfo = fetchKakaoUserInfo(accessToken);
        log.info("Kakao User Info: {}", userInfo);

        return createOAuth2User(userRequest, userInfo);

      } catch (Exception e) {
        log.error("Failed to handle Kakao OAuth2 token exchange", e);
        throw e;
      }
    }

    // 기본 처리 (Google, Naver 등)
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
    String email = extractEmailFromProvider(registrationId, attributes);

    return createOAuth2User(userRequest, attributes);
  }


  private Map<String, Object> fetchKakaoUserInfo(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    String userInfoUri = "https://kapi.kakao.com/v2/user/me";
    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
    if (response.getBody() == null) {
      throw new OAuth2AuthenticationException(new OAuth2Error("invalid_userinfo_response", "User info response is null or invalid", null));
    }
    return response.getBody();
  }

  private OAuth2User createOAuth2User(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String email = extractEmailFromProvider(registrationId, attributes);

    if (email == null) {
      throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found", "Email not found in provider attributes", null));
    }

    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      log.info("Existing user found: {}", user.getEmail());

      attributes.put("email", email);
      attributes.put("name", extractNameFromProvider(registrationId, attributes));
      attributes.put("nickname", extractNicknameFromProvider(registrationId, attributes));

      return new DefaultOAuth2User(
              Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getRole())),
              attributes,
              "email"
      );
    }

    // 비회원 처리: 사용자 정보를 세션에 저장
//    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    log.info("OAuth Attributes: {}", attributes);
//    log.info("Registration ID: {}", registrationIdFromAttributes);
//    log.info("Email: {}", email);
//    log.info("Name: {}", name);
//    log.info("Nickname: {}", nickname);
//
//    request.getSession().setAttribute("oauthAttributes", attributes);
//    request.getSession().setAttribute("registrationId", registrationIdFromAttributes);
//    request.getSession().setAttribute("email", email);
//    request.getSession().setAttribute("name", name);
//    request.getSession().setAttribute("nickname", nickname);
//
//    attributes.put("email", email); // 비회원도 email 속성 추가
//    return new DefaultOAuth2User(
//            Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST")),
//            attributes,
//            "email"
//    );
//  }

    // 비회원 처리
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    request.getSession().setAttribute("oauthAttributes", attributes);
    request.getSession().setAttribute("registrationId", registrationId);
    request.getSession().setAttribute("email", email);

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
      if (kakaoAccount != null) {
        log.info("Kakao Account Details: {}", kakaoAccount);
        String email = (String) kakaoAccount.get("email");
        if (email == null) {
          log.warn("Kakao email is null. Kakao Account: {}", kakaoAccount);
        }
        return email;
      } else {
        log.warn("Kakao Account is null. Attributes: {}", attributes);
      }
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      if (response != null) {
        log.info("Naver Response Details: {}", response);
        return (String) response.get("email");
      } else {
        log.warn("Naver Response is null. Attributes: {}", attributes);
      }
    }
    log.error("Email could not be extracted for provider: {}", registrationId);
    return null;
  }

  private String extractNameFromProvider(String registrationId, Map<String, Object> attributes) {
    log.debug("Extracting name for registrationId: {}", registrationId);
    if ("google".equals(registrationId)) {
      return (String) attributes.get("name");
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
      if (profile != null) {
        log.info("Kakao Profile Details: {}", profile);
        String name = (String) profile.get("nickname");
        if (name == null) {
          log.warn("Kakao nickname is null. Profile: {}", profile);
        }
        return name;
      } else {
        log.warn("Kakao Profile is null. Kakao Account: {}", kakaoAccount);
      }
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      if (response != null) {
        log.info("Naver Response Details: {}", response);
        return (String) response.get("name");
      } else {
        log.warn("Naver Response is null. Attributes: {}", attributes);
      }
    }
    log.error("Name could not be extracted for provider: {}", registrationId);
    return null;
  }

  private String extractNicknameFromProvider(String registrationId, Map<String, Object> attributes) {
    log.debug("Extracting nickname for registrationId: {}", registrationId);
    if ("google".equals(registrationId)) {
      return (String) attributes.get("name"); // Google uses the name as nickname
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
      if (profile != null) {
        log.info("Kakao Profile Details: {}", profile);
        String nickname = (String) profile.get("nickname");
        if (nickname == null) {
          log.warn("Kakao nickname is null. Profile: {}", profile);
        }
        return nickname;
      } else {
        log.warn("Kakao Profile is null. Kakao Account: {}", kakaoAccount);
      }
    } else if ("naver".equals(registrationId)) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      if (response != null) {
        log.info("Naver Response Details: {}", response);
        return (String) response.get("nickname");
      } else {
        log.warn("Naver Response is null. Attributes: {}", attributes);
      }
    }
    log.error("Nickname could not be extracted for provider: {}", registrationId);
    return null;
  }

}