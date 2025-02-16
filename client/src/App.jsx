import React, { useState } from 'react';
import axios from 'axios';

function App() {
    const [form, setForm] = useState({ name: '', email: '', message: '' });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/form/submit', form, {
                responseType: 'blob',
            });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'application_form.pdf');
            document.body.appendChild(link);
            link.click();
            alert('PDF downloaded and sent via email!');
        } catch (error) {
            console.error('Failed to submit form', error);
        }
    };

    return (
        <div>
            <h2>Application Form</h2>
            <form onSubmit={handleSubmit}>
                <input name="name" placeholder="Name" onChange={handleChange} required />
                <input name="email" placeholder="Email" onChange={handleChange} required />
                <textarea name="message" placeholder="Message" onChange={handleChange} required />
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default App;

