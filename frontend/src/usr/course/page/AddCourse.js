// src/pages/AddCourse.js
import React, { useState } from 'react';
import axios from 'axios';

const AddCourse = ({ token }) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [mapCoords, setMapCoords] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/usr/datecourse', {
        title,
        content,
        imageUrl,
        mapCoords,
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert('코스가 추가되었습니다.');
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="제목" />
      <textarea value={content} onChange={(e) => setContent(e.target.value)} placeholder="본문" />
      <input type="text" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} placeholder="이미지 URL" />
      <input type="text" value={mapCoords} onChange={(e) => setMapCoords(e.target.value)} placeholder="위도, 경도" />
      <button type="submit">코스 추가</button>
    </form>
  );
};

export default AddCourse;
