// 데이트코스 추가
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from '../modal/Modal';

function AddDateCourse() {
    const [course, setCourse] = useState({
        name: '',
        location: '',
        description: '',
        image: null,
        ageGroup: '',
        difficulty: '',
        options: []
    });
    const [isModalOpen, setModalOpen] = useState(false);
    const [modalMessage, setModalMessage] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCourse({
            ...course,
            [name]: value
        });
    };

    const handleImageChange = (e) => {
        setCourse({
            ...course,
            image: e.target.files[0]
        });
    };

    const handleOptionsChange = (e) => {
        const { value, checked } = e.target;
        const { options } = course;
        if (checked) {
            setCourse({
                ...course,
                options: [...options, value]
            });
        } else {
            setCourse({
                ...course,
                options: options.filter(option => option !== value)
            });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData();
        for (const key in course) {
            if (key === 'options') {
                course[key].forEach(option => formData.append('options', option));
            } else {
                formData.append(key, course[key]);
            }
        }
        axios.post('/api/usr/course/datecourses/fixed', formData)
            .then(response => {
                setModalMessage('코스가 성공적으로 추가되었습니다!');
                setModalOpen(true);
                setCourse({
                    name: '',
                    location: '',
                    description: '',
                    image: null,
                    ageGroup: '',
                    difficulty: '',
                    options: []
                });
            })
            .catch(error => console.error('Error saving data: ', error));
    };

    const handleModalClose = () => {
        setModalOpen(false);
        navigate('/datecourses'); // 원하는 경로로 이동
    };

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">코스 추가</h1>
            <form onSubmit={handleSubmit} className="space-y-4">
                <input
                    type="text"
                    name="name"
                    value={course.name}
                    onChange={handleInputChange}
                    placeholder="코스 이름"
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                />
                <input
                    type="text"
                    name="location"
                    value={course.location}
                    onChange={handleInputChange}
                    placeholder="코스 장소"
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                />
                <textarea
                    name="description"
                    value={course.description}
                    onChange={handleInputChange}
                    placeholder="코스 상세 설명"
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                ></textarea>
                <input
                    type="file"
                    name="image"
                    onChange={handleImageChange}
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                />
                <select
                    name="ageGroup"
                    value={course.ageGroup}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                >
                    <option value="">추천 연령대</option>
                    <option value="10">10대</option>
                    <option value="20">20대</option>
                    <option value="30">30대</option>
                    <option value="40">40대</option>
                </select>
                <select
                    name="difficulty"
                    value={course.difficulty}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border rounded-lg"
                >
                    <option value="">코스 난이도</option>
                    <option value="easy">쉬움</option>
                    <option value="medium">보통</option>
                    <option value="hard">어려움</option>
                </select>
                <div className="space-y-2">
                    <label className="flex items-center">
                        <input
                            type="checkbox"
                            value="nightview"
                            onChange={handleOptionsChange}
                            className="mr-2"
                        />
                        야경
                    </label>
                    <label className="flex items-center">
                        <input
                            type="checkbox"
                            value="nature"
                            onChange={handleOptionsChange}
                            className="mr-2"
                        />
                        자연
                    </label>
                    <label className="flex items-center">
                        <input
                            type="checkbox"
                            value="historical"
                            onChange={handleOptionsChange}
                            className="mr-2"
                        />
                        역사적인 장소
                    </label>
                </div>
                <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 transition duration-200">
                    코스 추가
                </button>
            </form>
            <Modal isOpen={isModalOpen} message={modalMessage} onClose={handleModalClose} />
        </div>
    );
}

export default AddDateCourse;
