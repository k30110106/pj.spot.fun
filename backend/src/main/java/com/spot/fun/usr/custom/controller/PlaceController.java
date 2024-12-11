package com.spot.fun.usr.custom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spot.fun.usr.custom.dto.PlaceDTO;
import com.spot.fun.usr.custom.service.PlaceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/usr/place")
public class PlaceController {
    private final PlaceService service;

    @GetMapping("/search")
    public List<PlaceDTO> searchPlaces(@RequestParam("address") String address) {
        return service.searchPlacesByAddress(address);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PlaceDTO>> getAllPlaces() {
        List<PlaceDTO> places = service.getAllPlaces();
        return ResponseEntity.ok(places);
    }
     
    
    
    
}
