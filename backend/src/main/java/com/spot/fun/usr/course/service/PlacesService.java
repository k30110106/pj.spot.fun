package com.spot.fun.usr.course.service;

import com.spot.fun.usr.course.model.DatePlaces;
import com.spot.fun.usr.course.repository.DatePlaceRepository;
import com.spot.fun.usr.custom.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacesService {

  @Autowired
  private DatePlaceRepository datePlaceRepository;

  public List<DatePlaces> getPlacesByIds(List<Long> placeIds) {
    return datePlaceRepository.findAllById(placeIds);
  }

  public List<DatePlaces> getAllPlaces() {
    return datePlaceRepository.findAll();
  }

  public DatePlaces savePlace(DatePlaces place) {
    return null;
  }


//  public List<DatePlaces> getPlaces(Double latMin, Double latMax, Double lngMin, Double lngMax) {
//    return datePlaceRepository.f(latMin, latMax, lngMin, lngMax);

//  }

}
