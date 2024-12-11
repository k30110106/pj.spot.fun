package com.spot.fun.usr.custom.service;

import java.util.List;

import com.spot.fun.usr.custom.dto.PlaceDTO;

public interface PlaceService {
  List<PlaceDTO> getAllPlaces();

  List<PlaceDTO> searchPlacesByAddress(String address);
  
}
