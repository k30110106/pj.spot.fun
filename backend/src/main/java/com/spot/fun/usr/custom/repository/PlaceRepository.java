package com.spot.fun.usr.custom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spot.fun.usr.custom.domain.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
  List<Place> findByAddressContainingIgnoreCase(String address);
}
