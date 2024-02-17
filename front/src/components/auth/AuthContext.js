import React, {createContext, useCallback, useContext, useEffect, useMemo, useState} from 'react';
import {parseJwt} from "../misc/Helpers";

export const AuthContext = createContext();

function AuthProvider({children}) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        setUser(storedUser);
    }, []);

    const getUser = useCallback(() => {
        return localStorage.getItem('user');
    }, []);

    const userIsAuthenticated = useCallback(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser === null) {
            return false;
        }

        if (!storedUser) {
            return false;
        }
        const jwt = parseJwt(storedUser);
        return jwt.exp >= Date.now() / 1000;
    }, []);

    const userLogin = useCallback((user) => {
        console.log("user: " + user);
        localStorage.setItem('user', user);
        setUser(user);
    }, []);

    const userLogout = useCallback(() => {
        localStorage.removeItem('user');
        setUser(null);
    }, []);

    const contextValue = useMemo(() => ({
        user,
        getUser,
        userIsAuthenticated,
        userLogin,
        userLogout,
    }), [user, getUser, userIsAuthenticated, userLogin, userLogout]);

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}

export {AuthProvider};
