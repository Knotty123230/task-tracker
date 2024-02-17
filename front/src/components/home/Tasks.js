// Tasks.js
import React, {useEffect, useState} from 'react';
import {DragDropContext, Draggable, Droppable} from 'react-beautiful-dnd';
import {useTasks} from '../misc/TaskContext';
import {Navigate, useNavigate} from 'react-router-dom';
import {useAuth} from "../auth/AuthContext";
import {TaskApi} from "../misc/TaskApi";
import "../style/Tasks.css"; // Make sure your CSS file path is correct

const statuses = ["To Do", "In Progress", "Done"];

function Tasks() {
    const auth = useAuth();
    const {fetchTasks} = useTasks();
    const isAuth = auth.userIsAuthenticated();
    const navigate = useNavigate();
    const [columns, setColumns] = useState({
        "To Do": [],
        "In Progress": [],
        "Done": []
    });

    useEffect(() => {
        if (isAuth) {
            fetchTasks().then((fetchedTasks) => {
                const statusColumns = {
                    "To Do": [],
                    "In Progress": [],
                    "Done": []
                };
                fetchedTasks.forEach(task => {
                    const statusGroup = task.status === 'TODO' ? "To Do" : task.status === 'PROGRESS' ? "In Progress" : "Done";
                    statusColumns[statusGroup].push(task);
                });
                setColumns(statusColumns);
            });
        }
    }, [isAuth, fetchTasks]);

    const onDragEnd = (result) => {
        if (!result.destination) {
            return;
        }
        const {source, destination} = result;
        const newColumns = {...columns};
        const [movedTask] = newColumns[source.droppableId].splice(source.index, 1);
        newColumns[destination.droppableId].splice(destination.index, 0, movedTask);

        // Make sure the status matches the backend enum values
        let newStatus = '';
        if (destination.droppableId === 'To Do') {
            newStatus = 'TODO';
        } else if (destination.droppableId === 'In Progress') {
            newStatus = 'PROGRESS';
        } else if (destination.droppableId === 'Done') {
            newStatus = 'DONE';
        }

        movedTask.status = newStatus;

        TaskApi.editTask(movedTask, auth.getUser()).then(() => {
            fetchTasks().then((updatedTasks) => {
                // Update columns state with new tasks
                // You should update the columns state with the fetched updated tasks here
                const updatedColumns = {
                    "To Do": [],
                    "In Progress": [],
                    "Done": []
                };
                updatedTasks.forEach(task => {
                    const statusGroup = task.status === 'TODO' ? "To Do" : task.status === 'PROGRESS' ? "In Progress" : "Done";
                    updatedColumns[statusGroup].push(task);
                });
                setColumns(updatedColumns);
            });
        }).catch(error => {
            console.error("Failed to update task status", error);
        });

        setColumns(newColumns);
    };

    if (!isAuth) {
        return <Navigate to="/login"/>;
    }

    return (
        <div className="tasksBoard">
            <h1>Tasks</h1>
            <button onClick={() => navigate('/create')}>Create Task</button>
            <DragDropContext onDragEnd={onDragEnd}>
                {statuses.map((status, statusIndex) => (
                    <Droppable key={status} droppableId={status}>
                        {(provided, snapshot) => (
                            <div
                                className={`column ${snapshot.isDraggingOver ? 'dragOver' : ''}`}
                                ref={provided.innerRef}
                                {...provided.droppableProps}
                            >
                                <h2>{status}</h2>
                                <div className="taskList">
                                    {columns[status].map((task, index) => (
                                        <Draggable key={task.id} draggableId={task.id} index={index}>
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    className={`taskItem ${snapshot.isDragging ? 'dragging' : ''}`}
                                                >
                                                    <div className="taskContent">
                                                        <p>{task.name}</p>
                                                        <span>{task.description}</span>
                                                        <div className="taskMeta">
                                                            <span>{new Date(task.createdAt).toLocaleDateString()}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            )}
                                        </Draggable>
                                    ))}
                                    {provided.placeholder}
                                </div>
                            </div>
                        )}
                    </Droppable>
                ))}
            </DragDropContext>
        </div>
    );
}

export default Tasks;
