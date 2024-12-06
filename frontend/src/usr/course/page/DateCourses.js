// src/pages/DateCourses.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Map from '../components/Map';

const DateCourses = () => {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/datecourse/public')
      .then(response => setCourses(response.data))
      .catch(error => console.error(error));
  }, []);
  useEffect(()=> {
   // 서버에서 모든 코스를 조회하는 API 호출
          axios.get("http://localhost:8080/api/datecourse/all")
              .then(response => {
                  setCourses(response.data);  // 가져온 데이터를 상태에 저장
              })
              .catch(error => {
                  console.error("Error fetching courses:", error);
              });
      }, []);  // 컴포넌트가 처음 렌더링될 때만 실행

  return (
    <div>
      <h1>데이트 코스</h1>
      <div>
        {courses.map(course => (
          <div key={course.id}>
            <h2>{course.title}</h2>
            <p>{course.content}</p>
            <Map coordinates={course.mapCoords.split(',').map(coord => parseFloat(coord))} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default DateCourses;