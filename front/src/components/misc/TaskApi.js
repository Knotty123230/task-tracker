import axios from 'axios'
import {parseJwt} from './Helpers'

export const TaskApi = {
    authenticate,
    signup,
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
// function allTasks() {
//     return instance.get('task'{
//         headers: {
//             'Content-type': 'application/json',
//             'Authorization': bearerAuth(user)
//         }
//     })
// }


const instance = axios.create({
    baseURL: 'http://localhost:8081/api'
})


instance.interceptors.request.use(function (config) {
    if (config.headers.Authorization) {
        const token = config.headers.Authorization.split(' ')[1]
        const data = parseJwt(token)
        if (Date.now() > data.exp * 1000) {
            window.location.href = "/login"
        }
    }
    return config
}, function (error) {
    return Promise.reject(error)
})

// -- Helper functions

function bearerAuth(user) {
    return `Bearer ${user.id_token}`
}