import React from 'react';
import { Link } from 'react-router-dom'; // Link 컴포넌트 임포트
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
      {/* Link 컴포넌트로 /datecourses 페이지로 이동 */}
      <span className='text-base pr-1'>추천코스</span>
      <Link to="/datecourses" className="text-xl text-gray-400">
        <span className='bg-gray-200 rounded-3xl pl-2 pr-2 pb-1'>&gt;</span>
      </Link>
      <SliderComponent items={recommendedCourses}></SliderComponent>
    </div>
  );
};

export default RecommendedCourses;
