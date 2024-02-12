// src/TasksContext.js

import React, { createContext, useContext, useState } from 'react';

const TasksContext = createContext();

export const useTasks = () => useContext(TasksContext);

export const TasksProvider = ({ children }) => {
    const [tasks, setTasks] = useState([]);

    const addTask = task => {
        setTasks(currentTasks => [...currentTasks, task]);
    };

    return (
        <TasksContext.Provider value={{ tasks, addTask }}>
            {children}
        </TasksContext.Provider>
    );
};
