// 추천 NAV 
import React from 'react';
import { ReactComponent as FoodFork } from '../icons/food-fork.svg';
import { ReactComponent as HomeState } from '../icons/home-state.svg';
import { ReactComponent as Pack } from '../icons/Pack.svg';
import { ReactComponent as LikeSpot } from '../icons/like-spots.svg';
import { ReactComponent as MakeCourse } from '../icons/make-course.svg'
import { ReactComponent as Feed } from '../icons/feed.svg';

const categories = [
  { name: '맛집', img: <FoodFork /> },
  { name: '관광', img: <HomeState /> },
  { name: '숙소', img: <Pack /> },
  { name: '펀추천코스', img: <LikeSpot /> },
  { name: '코스만들기', img: <MakeCourse /> },
  { name: '피드', img: <Feed /> },
  //   { name: '투어', icon: '🚶' },
  //   { name: '축제', icon: '🎉' },
];

const NavigationMenu = () => {
  return (

    <nav className="bg-gray-100 p-4 px-5 mt-4">
      <div className="container mx-auto flex justify-between">
        <div className='text-center justify-center p-3 font-bold'>
          <span className='text-custom-cyan font-bold'>Fun </span>Menu
        </div>
        {categories.map((category, index) => (
          <a href={category.name} key={index} className="text-center cursor-pointer">
            {/* SVG 컴포넌트를 직접 렌더링 */}
            <div style={{ width: '100px', height: '32px' }}> {category.img}
              <div className='text-xs'>{category.name}</div>
            </div>
          </a>
        ))}
      </div>
    </nav>
  );
};

export default NavigationMenu;
