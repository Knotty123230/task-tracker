import React, {createContext, useContext, useEffect, useState} from 'react'
import {parseJwt} from "../misc/Helpers";

export const AuthContext = createContext()

function AuthProvider({children}) {
    const [user, setUser] = useState(null)

    useEffect(() => {
        const storedUser = localStorage.getItem('user')
        setUser(storedUser)
    }, [])

    const getUser = () => {
        console.log(localStorage.getItem('user'))

        return localStorage.getItem('user')

    }

    const userIsAuthenticated = () => {
        const storedUser = localStorage.getItem('user');
        if (storedUser === null) {
            return false
        }

        if (!storedUser) {
            return false;
        }
        const jwt = parseJwt(storedUser);
        if (jwt.exp < Date.now() / 1000) {
            return false
        }

        return storedUser;

    }


    const userLogin = user => {
        console.log("user : " + user)
        localStorage.setItem('user', user)
        setUser(user)
    }

    const userLogout = () => {
        localStorage.removeItem('user')
        setUser(null)
    }

    const contextValue = {
        user,
        getUser,
        userIsAuthenticated,
        userLogin,
        userLogout,
    }

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    )
}


export function useAuth() {
    return useContext(AuthContext)
}

export {AuthProvider}