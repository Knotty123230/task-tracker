import React, {useState} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {useTasks} from '../misc/TaskContext';
import {TaskApi} from "../misc/TaskApi";
import {useAuth} from "../auth/AuthContext";

const EditTask = () => {
    const auth = useAuth();
    const isAuth = auth.userIsAuthenticated();
    let user;
    if (isAuth) {
        user = auth.getUser();
    }
    const navigate = useNavigate();
    const {state} = useLocation();
    console.log(state);
    const {fetchTasks} = useTasks();
    const [name, setName] = useState(state.task.name);
    const [description, setDescription] = useState(state.task.description);
    const [status, setStatus] = useState(state.task.status);

    const handleSave = async (e) => {
        e.preventDefault(); // Prevent page reload
        const updatedTask = {
            ...state.task,
            name,
            description,
            status,
        };
        console.log(updatedTask);
        await TaskApi.editTask(updatedTask, user);
        fetchTasks();
        navigate('/tasks');
    };

    return (
        <div>
            <h1>Editing Task</h1>
            <form onSubmit={handleSave}>
                <table>
                    <tbody>
                    <tr>
                        <td><label htmlFor="taskName">Name</label></td>
                        <td>
                            <input
                                id="taskName"
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="taskDescription">Description</label></td>
                        <td>
                                <textarea
                                    id="taskDescription"
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                    required
                                />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="taskStatus">Status</label></td>
                        <td>
                            <select
                                id="taskStatus"
                                value={status}
                                onChange={(e) => setStatus(e.target.value)}
                                required
                            >
                                <option value="PROGRESS">PROGRESS</option>
                                <option value="TODO">TODO</option>
                                <option value="DONE">DONE</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colSpan="2">
                            <button type="submit" style={{width: '100%'}}>Save Changes</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    );
};

export default EditTask;
