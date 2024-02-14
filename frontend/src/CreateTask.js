import axios from "axios";
import {useState} from "react";
import './CreateTask.css';

const CreateTask = ({onClose}) => {
    const [task, setTask] = useState({
        name: "",
        description: "",
    });


    const handleChange = (e) => {
        const {name, value} = e.target;
        setTask(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('http://localhost:8081/api/task', task)
            .then(response => {
                console.log(response);
                setTask({name: "", description: ""}); // Clear the form
                onClose(); // Close the modal after successful submission
            })
            .catch(error => {
                console.error("There was an error!", error);
            });
    };

    return (
        <div className="modal-container">
            <div className="modal-content">
                <button onClick={onClose} className="modal-close-button">X</button>
                {/* Close button */}
                <form onSubmit={handleSubmit}>
                    <div className="row">
                        <label htmlFor="name" className="larger-font">Name:</label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={task.name}
                            onChange={handleChange}
                            className="card-text"
                        />
                    </div>

                    <div className="row">
                        <label htmlFor="description" className="larger-font">Description:</label>
                        <textarea
                            id="description"
                            name="description"
                            value={task.description}
                            onChange={handleChange}
                            className="card-text"
                        />
                    </div>

                    <div className="row">
                        <button type="submit" className="card-text">Create Task</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateTask;
