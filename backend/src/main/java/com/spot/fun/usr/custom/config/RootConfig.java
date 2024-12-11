package com.spot.fun.usr.custom.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spot.fun.usr.custom.domain.Custom;
import com.spot.fun.usr.custom.domain.CustomPlace;
import com.spot.fun.usr.custom.dto.CustomDTO;
import com.spot.fun.usr.custom.dto.PlaceDTO;

@Configuration
public class RootConfig {
   @Bean
   public ModelMapper getMapper() {
      ModelMapper modelMapper = new ModelMapper();

      modelMapper.typeMap(Custom.class, CustomDTO.class).addMappings(mapper ->
              mapper.map(Custom::getTagList, CustomDTO::setTags)
      );
      modelMapper.typeMap(CustomPlace.class, PlaceDTO.class).addMappings(mapper ->
              mapper.map(src -> src.getPlace().getDescription(), PlaceDTO::setDescription)
      );
      modelMapper.typeMap(Custom.class, CustomDTO.class).addMappings(mapper ->
              mapper.skip(CustomDTO::setPlaces) // Places는 별도로 매핑하므로 skip
      );
      modelMapper.getConfiguration()
              .setFieldMatchingEnabled(true)
              .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
              .setMatchingStrategy(MatchingStrategies.LOOSE);

      return modelMapper;
   }
}
