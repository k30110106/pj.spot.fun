package com.spot.fun.usr.course.repository;


import com.spot.fun.usr.course.model.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {

  @Query("SELECT d FROM DateCourse d WHERE d.location = :location OR d.ageGroup = :ageGroup")
  List<DateCourse> findByLocationOrAgeGroup(@Param("location") String location, @Param("ageGroup") int ageGroup);

  List<DateCourse> findByFixed(@Param("fixed") boolean fixed);
  List<DateCourse> findByLocation(String location); // 예시: location에 해당하는 코스 찾기
}




