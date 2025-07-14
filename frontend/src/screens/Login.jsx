import React, { useState, useContext } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from '../config/axios'
import { UserContext } from '../context/user.context'

const Login = () => {


    const [ email, setEmail ] = useState('')
    const [ password, setPassword ] = useState('')

    const { setUser } = useContext(UserContext)

    const navigate = useNavigate()

    function submitHandler(e) {

        e.preventDefault()

        axios.post('/users/login', {
            email,
            password
        }).then((res) => {
            console.log(res.data)

            localStorage.setItem('token', res.data.token)
            setUser(res.data.user)

            navigate('/')
        }).catch((err) => {
            console.log(err.response.data)
        })
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 to-purple-200">
            <div className="bg-white p-10 rounded-2xl shadow-2xl w-full max-w-md flex flex-col items-center">
                <div className="mb-6 flex flex-col items-center">
                    <i className="ri-robot-2-line text-5xl text-blue-500 mb-2"></i>
                    <h2 className="text-3xl font-extrabold text-blue-700 mb-1">VAgent Login</h2>
                    <p className="text-gray-400">Welcome back! Please login to continue.</p>
                </div>
                <form
                    onSubmit={submitHandler}
                >
                    <div className="mb-4">
                        <label className="block text-gray-400 mb-2" htmlFor="email">Email</label>
                        <input

                            onChange={(e) => setEmail(e.target.value)}
                            type="email"
                            id="email"
                            className="w-full p-3 rounded bg-blue-50 text-gray-700 border border-blue-200 focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your email"
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block text-gray-400 mb-2" htmlFor="password">Password</label>
                        <input
                            onChange={(e) => setPassword(e.target.value)}
                            type="password"
                            id="password"
                            className="w-full p-3 rounded bg-blue-50 text-gray-700 border border-blue-200 focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your password"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full p-3 rounded bg-gradient-to-r from-blue-500 to-purple-500 text-white font-bold hover:from-blue-600 hover:to-purple-600 focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-lg transition-all"
                    >
                        <i className="ri-login-box-line mr-2"></i>Login
                    </button>
                </form>
                <p className="text-gray-500 mt-4">
                    Don't have an account? <Link to="/register" className="text-blue-600 hover:underline font-semibold">Create one</Link>
                </p>
            </div>
        </div>
    )
}

export default Login;