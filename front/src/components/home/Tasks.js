import React, {useEffect, useState} from 'react';
import {useAuth} from "../auth/AuthContext";
import {Navigate} from "react-router-dom";
import {TaskApi} from "../misc/TaskApi";

function Tasks() {
    const Auth = useAuth();
    const isAuthenticated = Auth.userIsAuthenticated();
    let user;
    if (!isAuthenticated === false) {
        user = Auth.getUser();
    }
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const response = await TaskApi.allTasks(user);
                setTasks(response.data);
            } catch (error) {
                console.error("Failed to fetch tasks", error);
            }
        };

        fetchTasks().then(r => console.log(r));
    }, []);

    if (!isAuthenticated) {
        return <Navigate to="/login"/>;
    }


    return (
        <div>
            <h1>Tasks</h1>
            <table>
                <tbody>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                </tr>
                {tasks.map((task) => (
                    <tr key={task.id}>
                        <td>{task.name}</td>
                        <td>{task.description}</td>
                        <td>{task.status}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default Tasks;
