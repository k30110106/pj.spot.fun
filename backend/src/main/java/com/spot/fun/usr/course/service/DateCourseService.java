package com.spot.fun.usr.course.service;

import com.spot.fun.usr.course.model.DateCourse;
import com.spot.fun.usr.course.repository.DateCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DateCourseService {

    @Autowired
    private DateCourseRepository dateCourseRepository;

    // 코스를 추가하는 메소드
    public void addCourse(DateCourse dateCourse) {
        // 유효성 검사나 추가적인 로직이 필요하면 여기에 추가
        dateCourseRepository.save(dateCourse);  // 코스 저장
    }

    // 모든 코스 조회
    public List<DateCourse> getAllCourses() {
        return dateCourseRepository.findAll();
    }

    // 특정 코스 조회
    public DateCourse getCourseById(Long id) {
        return dateCourseRepository.findById(id).orElse(null);
    }
}
