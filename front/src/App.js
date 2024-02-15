import React from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './components/auth/AuthContext'
import Login from './components/home/Login'
import Signup from "./components/home/Signup";
function App() {
  return (
      <AuthProvider>
          <Router>
              <Routes>
                  <Route path='/login' element={<Login />} />
                  <Route path='/signup' element={<Signup />} />
                  <Route path="*" element={<Navigate to="/" />} />
              </Routes>
          </Router>
      </AuthProvider>
  );
}

export default App;
