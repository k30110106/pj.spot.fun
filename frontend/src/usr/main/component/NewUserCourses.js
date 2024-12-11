// 사용자가 만든 데이트코스
import React from 'react';
import SliderComponent from './SliderComponent';
import { Link } from 'react-router-dom';

const newUserCourses = [
  { title: '유저 코스 1', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 2', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 3', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 4', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 5', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 6', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 7', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 8', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 9', image: 'https://via.placeholder.com/150' },
  { title: '유저 코스 10', image: 'https://via.placeholder.com/150' },
];

const NewUserCourses = () => {
  return (
    <section className="container mx-auto p-4 font-bold">
      <span className="text-xl font-bold mb-4 text-custom-cyan">New</span>
      <span className='text-base pr-1 pl-1'>최근 유저들이 만든 코스에요</span>
      <Link to="/custom" className="text-xl text-gray-400">
        <span className='bg-gray-200 rounded-3xl pl-2 pr-2 pb-1'>&gt;</span>
      </Link>
      <SliderComponent items={newUserCourses} />
    </section>
  );
};

export default NewUserCourses;