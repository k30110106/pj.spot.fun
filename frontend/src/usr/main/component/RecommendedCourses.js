// 펀추천코스
import React from 'react';
import SliderComponent from './SliderComponent';

const recommendedCourses = [
  { title: '추천 코스 1', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 2', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 3', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 4', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 5', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 6', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 7', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 8', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 9', image: 'https://via.placeholder.com/150' },
  { title: '추천 코스 10', image: 'https://via.placeholder.com/150' },
];

const RecommendedCourses = () => {
  return (
    <div className="container mx-auto p-4 font-bold">
      <span className="text-xl font-bold mb-4 text-custom-cyan">Fun </span>
      추천코스
      <SliderComponent items={recommendedCourses} />
    </div>
  );
};

export default RecommendedCourses;