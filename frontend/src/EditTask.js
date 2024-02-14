// EditTask.js
import axios from 'axios';
import {useEffect, useState} from 'react';
import "./EditTask.css"

const EditTask = ({task, onClose}) => {
    const [editedTask, setEditedTask] = useState({...task});

    useEffect(() => {
        setEditedTask(task);
    }, [task]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setEditedTask(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleDeleteTask = () => {
        axios.delete(`http://localhost:8081/api/task/${task.id}`)
            .then(() => {
                onClose(null, true);
            })
            .catch(error => console.error("Failed to delete task", error));
    };


    const handleSubmit = (e) => {
        e.preventDefault();
        axios.put(`http://localhost:8081/api/task`, editedTask)
            .then(response => {
                console.log(response);
                onClose(editedTask);
            })
            .catch(error => {
                console.error("There was an error updating the task:", error);
            });
    };


    return (
        <div className="edit-task-modal">
            <div className="modal-content">
                <span className="close-button" onClick={() => onClose(false)}>&times;</span>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={editedTask.name}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label>Description:</label>
                        <textarea
                            name="description"
                            value={editedTask.description}
                            onChange={handleChange}
                        />
                    </div>
                    <button type="submit">Update Task</button>
                    <button onClick={handleDeleteTask}>Delete Task</button>
                </form>
            </div>
        </div>
    );
};

export default EditTask;
