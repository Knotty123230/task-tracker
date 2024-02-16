import React, { useState } from 'react'
import {NavLink, Navigate} from 'react-router-dom'
import { Button, Form, Grid, Segment, Message } from 'semantic-ui-react'
import { useAuth } from '../auth/AuthContext'
import {TaskApi} from '../misc/TaskApi'
import { handleLogError } from '../misc/Helpers'

function Login() {
    const Auth = useAuth()
    const isLoggedIn = Auth.userIsAuthenticated()

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [isError, setIsError] = useState(false)


    const handleInputChange = (e, { name, value }) => {
        if (name === 'username') {
            setUsername(value)
        } else if (name === 'password') {
            setPassword(value)
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!(username && password)) {
            setIsError(true)
            return
        }

        try {
            const response = await TaskApi.authenticate(username, password)


            Auth.userLogin(response.data.id_token)

            setUsername('')
            setPassword('')
            setIsError(false)
            return <Navigate to={'/tasks'} />
        } catch (error) {
            handleLogError(error)
            setIsError(true)
        }
    }

    if (isLoggedIn) {
        return <Navigate to={'/tasks'} />
    }

    return (
        <Grid textAlign='center'>
            <Grid.Column style={{ maxWidth: 450 }}>
                <Form size='large' onSubmit={handleSubmit}>
                    <Segment>
                        <Form.Input
                            fluid
                            autoFocus
                            name='username'
                            icon='user'
                            iconPosition='left'
                            placeholder='Username'
                            value={username}
                            onChange={handleInputChange}
                        />
                        <Form.Input
                            fluid
                            name='password'
                            icon='lock'
                            iconPosition='left'
                            placeholder='Password'
                            type='password'
                            value={password}
                            onChange={handleInputChange}
                        />
                        <Button type='submit' color='violet' fluid size='large'>Login</Button>
                    </Segment>
                </Form>
                <Message>{`Don't have already an account? `}
                    <NavLink to="/signup" color='violet' as={NavLink}>Sign Up</NavLink>
                </Message>
                {isError && <Message negative>The username or password provided are incorrect!</Message>}
            </Grid.Column>
        </Grid>
    )
}

export default Login