// 펀 인기 코스
import React from 'react';
import SliderComponent from './SliderComponent';

const popularCourses = [
  { title: '인기 코스 1', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 2', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 3', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 4', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 5', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 6', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 7', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 8', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 9', image: 'https://via.placeholder.com/150' },
  { title: '인기 코스 10', image: 'https://via.placeholder.com/150' },
];

const PopularCourses = () => {
  return (
    <section className="container mx-auto p-4 font-bold">
      <span className="text-xl font-bold mb-4 text-custom-cyan">Fun</span> 인기코스
      <SliderComponent items={popularCourses} />
    </section>
  );
};

export default PopularCourses;