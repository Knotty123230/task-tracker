import axios from 'axios'

export const TaskApi = {
    authenticate,
    signup,
    allTasks,
    createTask,
    deleteTask,
    editTask
}

function deleteTask(id, user) {
    return instance.delete(`/task/${id}`, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}

function editTask(task, user) {
    return instance.put(`/task`, task, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}

function authenticate(username, password) {
    return instance.post('/authenticate', {username, password}, {
        headers: {'Content-type': 'application/json'}
    })
}

function signup(user) {
    return instance.post('/registration', user, {
        headers: {'Content-type': 'application/json'}
    })
}

function allTasks(user) {
    return instance.get('/task', {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}

function createTask(task, user) {
    return instance.post('/task', task, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}


const instance = axios.create({
    baseURL: 'http://localhost:8081/api'
})


instance.interceptors.request.use(function (config) {
    if (config.headers.Authorization) {
        const token = config.headers.Authorization.split(' ')[1]
        console.log(token)
    }
    return config
}, function (error) {
    return Promise.reject(error)
})

// -- Helper functions

function bearerAuth(user) {
    return `Bearer ${user}`
}