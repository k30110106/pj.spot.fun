package com.spot.fun.usr.oauthlogin.converter;

import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

  private final RestTemplate restTemplate;

  public CustomOAuth2AccessTokenResponseClient() {
    this.restTemplate = new RestTemplate();
    // Add converters to support application/x-www-form-urlencoded
    this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    this.restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
  }

  @Override
  public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
    String tokenUri = authorizationGrantRequest.getClientRegistration().getProviderDetails().getTokenUri();
    URI uri = URI.create(tokenUri);

    // Create token request body
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
    body.add(OAuth2ParameterNames.CODE, authorizationGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode());
    body.add(OAuth2ParameterNames.REDIRECT_URI, authorizationGrantRequest.getClientRegistration().getRedirectUri());
    body.add(OAuth2ParameterNames.CLIENT_ID, authorizationGrantRequest.getClientRegistration().getClientId());

    if (authorizationGrantRequest.getClientRegistration().getClientSecret() != null) {
      body.add(OAuth2ParameterNames.CLIENT_SECRET, authorizationGrantRequest.getClientRegistration().getClientSecret());
    }

    // Create token request headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, uri);

    try {
      // Send token request
      ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);

      Map<String, Object> tokenResponse = responseEntity.getBody();

      return OAuth2AccessTokenResponse.withToken((String) tokenResponse.get("access_token"))
              .tokenType(OAuth2AccessToken.TokenType.BEARER) // Token type is assumed to be Bearer
              .expiresIn(Long.parseLong(tokenResponse.get("expires_in").toString()))
              .scopes(authorizationGrantRequest.getClientRegistration().getScopes())
              .refreshToken((String) tokenResponse.get("refresh_token"))
              .build();
    } catch (Exception ex) {
      throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token_response", "Failed to retrieve token from Kakao", null), ex);
    }
  }
}
