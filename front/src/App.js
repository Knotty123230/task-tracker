// App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/home/Login';
import Signup from "./components/home/Signup";
import Tasks from "./components/home/Tasks";
import { AuthProvider } from "./components/auth/AuthContext";
import Logout from "./components/home/Logout";

function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path='/login' element={<Login />} />
                    <Route path='/logout' element={<Logout />} />
                    <Route path='/signup' element={<Signup />} />
                    <Route path='/tasks' element={<Tasks />} />
                    <Route path='/' element={<Tasks />} />
                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
