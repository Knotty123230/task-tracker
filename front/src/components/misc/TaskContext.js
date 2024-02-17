import React, {createContext, useCallback, useContext, useState} from 'react';
import {TaskApi} from "./TaskApi";
import {useAuth} from "../auth/AuthContext";

const TaskContext = createContext();

export const useTasks = () => useContext(TaskContext);

export const TaskProvider = ({children}) => {
    const [tasks, setTasks] = useState([]);
    const auth = useAuth();
    const isAuth = auth.userIsAuthenticated();
    let user;
    if (isAuth) {
        user = auth.getUser();
    }

    // Using useCallback to memoize fetchTasks
    const fetchTasks = useCallback(async () => {
        try {
            const response = await TaskApi.allTasks(user);
            console.log(response);
            setTasks(response.data);

            return response.data;
        } catch (error) {
            console.error("Failed to fetch tasks", error);
        }
    }, [user]); // Add user as a dependency if it's used inside fetchTasks

    return (
        <TaskContext.Provider value={{tasks, setTasks, fetchTasks}}>
            {children}
        </TaskContext.Provider>
    );
};
