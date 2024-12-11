package com.spot.fun.usr.custom.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.spot.fun.usr.custom.domain.Custom;
import com.spot.fun.usr.custom.domain.CustomPlace;
import com.spot.fun.usr.custom.domain.Place;
import com.spot.fun.usr.custom.dto.CustomDTO;
import com.spot.fun.usr.custom.dto.PlaceDTO;
import com.spot.fun.usr.custom.repository.CustomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CustomServiceImpl implements CustomService {
    private final ModelMapper modelMapper;
    private final CustomRepository customRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long register(CustomDTO customDTO) {
    Custom custom = Custom.builder()
        .title(customDTO.getTitle())
        .description(customDTO.getDescription())
        .tags(String.join(",", customDTO.getTags()))
        .build();

    List<CustomPlace> customPlaces = IntStream.range(0, customDTO.getPlaces().size())
    .mapToObj(index -> {
        PlaceDTO dto = customDTO.getPlaces().get(index);
        Place place = entityManager.find(Place.class, dto.getId());
        if (place == null) {
            throw new RuntimeException("Place not found with id: " + dto.getId());
        }
        CustomPlace customPlace = new CustomPlace();
        customPlace.setCustom(custom);
        customPlace.setPlace(place);
        customPlace.setOrderIndex(index); // 🔥 순서 보장
        return customPlace;
    })
    .collect(Collectors.toList());


    custom.setCustomPlaces(customPlaces);

    Custom saved = customRepository.save(custom);
    return saved.getCno();
}

    

@Override
public CustomDTO get(Long cno) {
    Custom custom = customRepository.findById(cno)
        .orElseThrow(() -> new RuntimeException("Custom not found for cno: " + cno));

    CustomDTO customDTO = modelMapper.map(custom, CustomDTO.class);
    
    // 🔥 CustomPlace의 Place를 DTO로 변환 (orderIndex 순으로 정렬)
    List<PlaceDTO> placeDTOs = custom.getCustomPlaces().stream()
        .sorted(Comparator.comparing(CustomPlace::getOrderIndex)) // 🟢 orderIndex로 정렬
        .map(customPlace -> modelMapper.map(customPlace.getPlace(), PlaceDTO.class)) // 🔥 CustomPlace의 Place만 매핑
        .collect(Collectors.toList());

    customDTO.setPlaces(placeDTOs);
    customDTO.setTags(custom.getTagList()); // 🔥 태그 매핑 추가

    return customDTO;
}

    @Override
    public List<CustomDTO> list() {
    List<Custom> customList = customRepository.findAll();
    
    return customList.stream().map(custom -> {
        // 1️⃣ CustomDTO 생성
        CustomDTO customDTO = modelMapper.map(custom, CustomDTO.class);
        
        // 2️⃣ CustomPlace를 orderIndex 기준으로 정렬 후 PlaceDTO로 변환
        List<PlaceDTO> placeDTOs = custom.getCustomPlaces().stream()
            .sorted(Comparator.comparing(CustomPlace::getOrderIndex)) // 🟢 orderIndex로 정렬
            .map(customPlace -> modelMapper.map(customPlace.getPlace(), PlaceDTO.class)) // 🔥 CustomPlace의 Place만 매핑
            .collect(Collectors.toList());

        // 3️⃣ CustomDTO에 places 설정
        customDTO.setPlaces(placeDTOs);
        customDTO.setTags(custom.getTagList());
        return customDTO;
    }).collect(Collectors.toList());
}

@Override
public void update(Long cno, CustomDTO customDTO) {
    Custom existingCustom = customRepository.findById(cno)
        .orElseThrow(() -> new RuntimeException("Custom not found for cno: " + cno));

    // 1️⃣ 코스 기본 정보 업데이트 (제목, 설명, 태그)
    existingCustom.setTitle(customDTO.getTitle());
    existingCustom.setDescription(customDTO.getDescription());
    existingCustom.setTags(String.join(",", customDTO.getTags())); // 태그 업데이트

    // 2️⃣ 장소 정보 업데이트
    existingCustom.getCustomPlaces().clear(); // 기존 장소 제거

    List<CustomPlace> newCustomPlaces = IntStream.range(0, customDTO.getPlaces().size())
    .mapToObj(index -> {
        PlaceDTO placeDTO = customDTO.getPlaces().get(index);
        Place place = modelMapper.map(placeDTO, Place.class);
        CustomPlace customPlace = CustomPlace.builder()
            .custom(existingCustom)
            .place(place)
            .orderIndex(index) // 🔥 인덱스 설정
            .build();
        return customPlace;
    })
    .collect(Collectors.toList());

    existingCustom.getCustomPlaces().addAll(newCustomPlaces);

    customRepository.save(existingCustom);
}

@Override
public void delete(Long cno) {
    Custom custom = customRepository.findById(cno)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 코스입니다."));

    customRepository.delete(custom); // 🔥 삭제 수행
}

    
}

