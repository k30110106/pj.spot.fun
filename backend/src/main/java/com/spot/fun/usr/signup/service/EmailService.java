package com.spot.fun.usr.signup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender mailSender;

  public String sendVerificationEmail(String email) {
    String verificationCode = generateVerificationCode();

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setFrom("bjioazz12@naver.com");
    message.setSubject("FunSpot 이메일 인증 코드");
    message.setText("인증 코드: " + verificationCode);

    mailSender.send(message);
    return verificationCode;
  }

  private String generateVerificationCode() {
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder verificationCode = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      verificationCode.append(secureRandom.nextInt(10));
    }
    return verificationCode.toString();
  }
}