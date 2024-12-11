package com.spot.fun.usr.custom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {
  private Long id;
  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private String description;
  private Integer estimatedCost;
  private Integer durationMinutes; 
  
}
