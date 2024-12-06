package com.spot.fun.usr.course.controller;

import com.spot.fun.usr.course.model.DateCourse;
import com.spot.fun.usr.course.repository.DateCourseRepository;
import com.spot.fun.usr.course.service.DateCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usr/datecourse")
public class DateCourseController {

    @Autowired
    private DateCourseRepository dateCourseRepository;

    @Autowired
    private DateCourseService dateCourseService;

    // 관리자만 고정 코스를 등록
    @PostMapping
    public DateCourse createCourse(@RequestBody DateCourse dateCourse) {
        return dateCourseRepository.save(dateCourse);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCourse(@RequestBody DateCourse dateCourse) {
        try {
            dateCourseService.addCourse(dateCourse); // 코스를 서비스에 전달하여 저장
            return ResponseEntity.ok("Course added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding course");
        }
    }

    // 모든 코스 조회
    @GetMapping("/all")
    public ResponseEntity<List<DateCourse>> getAllCourses() {
        List<DateCourse> courses = dateCourseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // 특정 코스 조회 (ID로 조회 예시)
    @GetMapping("/{id}")
    public ResponseEntity<DateCourse> getCourseById(@PathVariable Long id) {
        DateCourse course = dateCourseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 모든 사용자가 코스를 조회할 수 있음
    @GetMapping("/public")
    public List<DateCourse> getAllPublicCourses() {
        return dateCourseRepository.findAll();
    }

    // 사용자가 추가한 코스를 비회원 및 회원이 볼 수 있음
    @GetMapping("/user")
    public List<DateCourse> getUserCourses() {
        return dateCourseRepository.findAll();
    }
}
