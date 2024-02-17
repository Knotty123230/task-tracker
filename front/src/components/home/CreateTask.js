import {useState} from "react";
import {useAuth} from "../auth/AuthContext";
import {Navigate} from "react-router-dom";
import {TaskApi} from "../misc/TaskApi";

const CreateTask = () => {
    const Auth = useAuth();
    const isAuthenticated = Auth.userIsAuthenticated();
    let user;
    if (!isAuthenticated === false) {
        user = Auth.getUser();
    }

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [redirectToReferrer, setRedirectToReferrer] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!name || !description) {
            alert('Please fill in all fields');
            return;
        }
        try {
            await TaskApi.createTask({name, description}, user);
            setRedirectToReferrer(true);
        } catch (error) {
            console.error("Failed to create task", error);
        }
    };

    if (!isAuthenticated) {
        return <Navigate to="/login"/>;
    }

    if (redirectToReferrer) {
        return <Navigate to="/tasks"/>;
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="title"
                    placeholder="Title"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="text"
                    name="description"
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
                <button type="submit">Create</button>
            </form>
        </div>
    );
};

export default CreateTask;
