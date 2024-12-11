package com.spot.fun.usr.custom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spot.fun.usr.custom.domain.Custom;

public interface CustomRepository extends JpaRepository<Custom, Long> {
  Optional<Custom> findById(Long cno);
  
}
