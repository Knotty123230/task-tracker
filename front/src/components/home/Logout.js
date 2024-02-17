import {useState} from 'react';
import {useAuth} from "../auth/AuthContext";
import {Navigate} from "react-router-dom";

const Logout = () => {
    const [isLoggedOut, setIsLoggedOut] = useState(false);
    const Auth = useAuth();

    function handleLogout() {
        Auth.userLogout();
        setIsLoggedOut(true);
    }

    if (isLoggedOut) {
        return <Navigate to="/login"/>;
    }

    return (
        <header>
            <button onClick={handleLogout}>Logout</button>
        </header>
    );
};

export default Logout;
