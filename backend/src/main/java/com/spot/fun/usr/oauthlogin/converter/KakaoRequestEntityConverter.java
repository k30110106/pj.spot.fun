package com.spot.fun.usr.oauthlogin.converter;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Kakao 전용 RequestEntityConverter
 */
@Log4j2
public class KakaoRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {
  @Override
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest grantRequest) {
    log.info("Converting OAuth2AuthorizationCodeGrantRequest for Kakao: {}", grantRequest);
    RequestEntity<?> defaultRequestEntity = super.convert(grantRequest);

    if ("kakao".equals(grantRequest.getClientRegistration().getRegistrationId())) {
      log.info("Processing Kakao-specific token request...");

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      Map<String, String> body = new LinkedHashMap<>();
      body.put(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
      body.put(OAuth2ParameterNames.CODE, grantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode());
      body.put(OAuth2ParameterNames.REDIRECT_URI, grantRequest.getClientRegistration().getRedirectUri());
      body.put(OAuth2ParameterNames.CLIENT_ID, grantRequest.getClientRegistration().getClientId());

      if (grantRequest.getClientRegistration().getClientSecret() != null) {
        body.put(OAuth2ParameterNames.CLIENT_SECRET, grantRequest.getClientRegistration().getClientSecret());
      }

      log.info("Kakao token request headers: {}", headers);
      log.info("Kakao token request body: {}", body);

      return new RequestEntity<>(body, headers, defaultRequestEntity.getMethod(), defaultRequestEntity.getUrl());
    }

    return defaultRequestEntity;
    }
}