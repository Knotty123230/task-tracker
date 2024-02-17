// App.js
import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Login from './components/home/Login';
import Signup from "./components/home/Signup";
import Tasks from "./components/home/Tasks";
import {AuthProvider} from "./components/auth/AuthContext";
import Logout from "./components/home/Logout";
import CreateTask from "./components/home/CreateTask";
import EditTask from "./components/home/EditTask";
import {TaskProvider} from "./components/misc/TaskContext";

function App() {
    return (
        <AuthProvider>
            <TaskProvider>
                <Router>
                    <Routes>
                        <Route path='/login' element={<Login/>}/>
                        <Route path='/logout' element={<Logout/>}/>
                        <Route path='/signup' element={<Signup/>}/>
                        <Route path='/' element={<Tasks/>}/>
                        <Route path="*" element={<Navigate to="/"/>}/>
                        <Route path="/create" element={<CreateTask/>}/>
                        <Route path="/tasks" element={<Tasks/>}/>
                        <Route path="/edit" element={<EditTask/>}/>
                    </Routes>
                </Router>
            </TaskProvider>
        </AuthProvider>
    );
}

export default App;
