package com.spot.fun.usr.course.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatePlaces {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;         // 장소 이름
  private String description;  // 설명
  private Double latitude;     // 위도
  private Double longitude;    // 경도
  private String location;
  private String cost;         // 예상 가격
  private String time;         // 소요 시간


  @ManyToOne
  @JoinColumn(name = "course_id")
  @JsonBackReference
  private DateCourse course;

}

//@Getter
//@Setter
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class DatePlaces {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//  private String name;         // 장소 이름
//  private String description;  // 설명
//  private Double latitude;     // 위도
//  private Double longitude;    // 경도
//  private String cost;  // 예상 가격
//  private String time;  // 소요 시간
//
//  @ManyToOne
//  @JoinColumn(name = "course_id") // 외래 키 지정
//  private DateCourse course;
//  // Getters and Setters
//}
