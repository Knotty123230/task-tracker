import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {DragDropContext, Draggable, Droppable} from 'react-beautiful-dnd';
import EditTask from "./EditTask";
import {formatDate, truncateDescription} from './utils/utils';
import "./ListTask.css";

const ListTask = () => {
    const [tasks, setTasks] = useState({todo: [], inProgress: [], done: []});
    const [showEditModal, setShowEditModal] = useState(false);
    const [currentTask, setCurrentTask] = useState(null);

    useEffect(() => {
        axios.get('http://localhost:8081/api/task')
            .then(response => {
                // Assuming response.data is the array you've described
                const tasks = response.data.map(item => item.body); // Extract the body from each item
                const organizedTasks = {TODO: [], PROGRESS: [], DONE: []};

                tasks.forEach(task => {
                    const status = task.status.toUpperCase(); // Ensure the status is in uppercase to match your keys
                    if (organizedTasks.hasOwnProperty(status)) {
                        organizedTasks[status].push(task);
                    } else {
                        console.log("Unknown status:", status);
                    }
                });

                // console.log(organizedTasks);
                setTasks(organizedTasks);
            })
            .catch(error => console.log(error));
    }, []);

    const onDragEnd = (result) => {
        const {source, destination, draggableId} = result;

        if (!destination || (source.droppableId === destination.droppableId && source.index === destination.index)) {
            return; // Item didn't move or was moved outside of any list
        }

        const sourceKey = source.droppableId.toUpperCase();
        const destinationKey = destination.droppableId.toUpperCase();
        const movedTask = tasks[sourceKey].find(task => task.id === draggableId);

        if (movedTask) {
            // First, update the local state to reflect the drag-and-drop action
            const startTasks = tasks[sourceKey].filter(task => task.id !== draggableId);
            const finishTasks = [...tasks[destinationKey], movedTask];

            const newTasksState = {
                ...tasks,
                [sourceKey]: startTasks,
                [destinationKey]: finishTasks,
            };

            setTasks(newTasksState);

            // Then, update the task status on the server
            updateTaskStatus(movedTask, destinationKey);
        }
    };

    const updateTaskStatus = (taskToUpdate, newStatus) => {
        const updatedTask = {...taskToUpdate, status: newStatus};

        axios.put(`http://localhost:8081/api/task`, updatedTask)
            .catch(error => {
                console.error("Failed to update task status", error);
            });
    };


    const handleDoubleClick = (task) => {
        setCurrentTask(task);
        setShowEditModal(true);
    };

    const handleTaskUpdate = (updatedTask, isDeleted) => {
        setShowEditModal(false);
        if (isDeleted) {
            setTasks(prev => {
                const newTasks = {...prev};
                Object.keys(newTasks).forEach(status => {
                    newTasks[status] = newTasks[status].filter(task => task.id !== currentTask.id);
                });
                return newTasks;
            });
        }
        if (!updatedTask || !updatedTask.status) return; // Додано перевірку тут

        setTasks(prevTasks => {
            const newTasks = {...prevTasks};
            const status = updatedTask.status.toUpperCase();
            const taskIndex = newTasks[status].findIndex(t => t.id === updatedTask.id);

            if (taskIndex !== -1) {
                newTasks[status][taskIndex] = updatedTask;
            }

            return newTasks;
        });
    };


    return (
        <div className="task-tracker-container">
            <h1 className="text-center">Task Tracker</h1>
            <DragDropContext onDragEnd={onDragEnd}>
                {Object.keys(tasks).map((status, index) => (
                    <Droppable droppableId={status} key={index}>
                        {(provided) => (
                            <div
                                {...provided.droppableProps}
                                ref={provided.innerRef}
                                className="task-column"
                            >
                                <h2>{status}</h2>
                                {tasks[status].map((task, index) => (
                                    <Draggable key={task.id} draggableId={String(task.id)} index={index}>
                                        {(provided) => (
                                            <div
                                                ref={provided.innerRef}
                                                {...provided.draggableProps}
                                                {...provided.dragHandleProps}
                                                className="task-item"
                                                onDoubleClick={() => handleDoubleClick(task)} // Handle double click to edit task
                                            >
                                                <div>{task.name}</div>
                                                <div>{truncateDescription(task.description)}</div>
                                                <div>{formatDate(task.createdAt)}</div>
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}
                            </div>
                        )}
                    </Droppable>
                ))}
            </DragDropContext>
            {showEditModal && <EditTask task={currentTask} onClose={handleTaskUpdate}/>}
        </div>
    );
};
export default ListTask;
