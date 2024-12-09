package com.spot.fun.usr.course.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter // Lombok으로 자동으로 Getter와 Setter를 생성
public class DateCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // 코스 이름
    private String location;        // 위치
    private String description;     // 설명
    private int ageGroup;           // 나이 그룹 (예: 20대, 30대 등)
    private boolean fixed;          // 고정된 코스 여부 (변경 가능 여부)

    // 기본 생성자, Lombok이 자동으로 추가됨
}