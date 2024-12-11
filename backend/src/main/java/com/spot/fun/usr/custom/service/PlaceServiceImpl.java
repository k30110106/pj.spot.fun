package com.spot.fun.usr.custom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.spot.fun.usr.custom.domain.Place;
import com.spot.fun.usr.custom.dto.PlaceDTO;
import com.spot.fun.usr.custom.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PlaceDTO> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream()
                .map(place -> modelMapper.map(place, PlaceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDTO> searchPlacesByAddress(String address) {
        List<Place> places = placeRepository.findByAddressContainingIgnoreCase(address);
        return places.stream()
                .map(place -> modelMapper.map(place, PlaceDTO.class))
                .collect(Collectors.toList());
    }
    
}

