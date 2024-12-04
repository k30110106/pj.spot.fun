// 고정데이트코스 추가폼
import React, { useState } from 'react';
import axios from 'axios';

function AddFixedCourseForm({ onCourseAdded }) {
    const [newCourse, setNewCourse] = useState({ name: '', location: '' });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewCourse({
            ...newCourse,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('api/usr/course/datecourses/fixed', newCourse)
            .then(response => {
                onCourseAdded(response.data);
                setNewCourse({ name: '', location: '' });
            })
            .catch(error => console.error('Error saving data: ', error));
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                name="name"
                value={newCourse.name}
                onChange={handleInputChange}
                placeholder="코스 이름"
                required
            />
            <input
                type="text"
                name="location"
                value={newCourse.location}
                onChange={handleInputChange}
                placeholder="위치"
                required
            />
            <button type="submit">코스 추가</button>
        </form>
    );
}

export default AddFixedCourseForm;