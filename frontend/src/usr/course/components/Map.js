// src/components/Map.js
import React, { useEffect } from 'react';

const Map = ({ coordinates }) => {
  useEffect(() => {
    const map = new window.naver.maps.Map('map', {
      center: new window.naver.maps.LatLng(coordinates[0], coordinates[1]),
      zoom: 10,
    });

    // 지도에 핀 추가
    new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(coordinates[0], coordinates[1]),
      map: map,
    });

    // 경로 표시 예시: 여러 지점 추가
    const path = [
      new window.naver.maps.LatLng(coordinates[0], coordinates[1]),
      new window.naver.maps.LatLng(coordinates[0] + 0.1, coordinates[1] + 0.1), // 경로 예시
    ];
    new window.naver.maps.Polyline({
      path: path,
      strokeColor: '#ff0000',
      strokeOpacity: 1,
      strokeWeight: 3,
      map: map,
    });
  }, [coordinates]);

  return <div id="map" style={{ width: '100%', height: '500px' }}></div>;
};

export default Map;
