import axios from 'axios'
import {parseJwt} from './Helpers'

export const TaskApi = {
    authenticate,
    signup,
    allTasks
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