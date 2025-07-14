import React, { useContext, useState, useEffect } from 'react'
import { UserContext } from '../context/user.context'
import axios from "../config/axios"
import { useNavigate } from 'react-router-dom'

const Home = () => {

    const { user, setUser } = useContext(UserContext)
    const [ isModalOpen, setIsModalOpen ] = useState(false)
    const [ isEditModalOpen, setIsEditModalOpen ] = useState(false)
    const [ projectName, setProjectName ] = useState("")
    const [ projects, setProjects ] = useState([])
    const [ selectedProject, setSelectedProject ] = useState(null)

    const navigate = useNavigate()

    function createProject(e) {
        e.preventDefault()
        console.log({ projectName })

        axios.post('/projects/create', {
            name: projectName,
        })
            .then((res) => {
                console.log(res)
                setProjects(prevProjects => [...prevProjects, res.data])
                setIsModalOpen(false)
                setProjectName('')
            })
            .catch((error) => {
                console.log(error)
            })
    }

    function updateProject(e) {
        e.preventDefault()
        
        if (!selectedProject) return;

        axios.put('/projects/update-name', {
            projectId: selectedProject._id,
            name: projectName,
        })
            .then((res) => {
                console.log(res)
                setProjects(prevProjects => 
                    prevProjects.map(p => 
                        p._id === selectedProject._id ? res.data.project : p
                    )
                )
                setIsEditModalOpen(false)
                setProjectName('')
                setSelectedProject(null)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    function deleteProject(projectId) {
        if (!projectId) return;

        const confirmed = window.confirm("Are you sure you want to delete this project?");
        if (!confirmed) return;

        axios.delete(`/projects/delete/${projectId}`)
            .then((res) => {
                console.log(res)
                setProjects(prevProjects => 
                    prevProjects.filter(p => p._id !== projectId)
                )
            })
            .catch((error) => {
                console.log(error)
            })
    }

    const openEditModal = (project) => {
        setSelectedProject(project);
        setProjectName(project.name);
        setIsEditModalOpen(true);
    };

    useEffect(() => {
        if (!user) return;

        axios.get('/projects/all')
            .then((res) => {
                setProjects(res.data.projects);
            })
            .catch(err => {
                console.log(err);
            });
    }, [user]);

    // Logout function
    function handleLogout() {
        localStorage.removeItem('token');
        setUser(null);
        navigate('/login');
    }

    return (
        <main className='relative min-h-screen bg-gradient-to-br from-blue-50 to-purple-100 p-4'>
            {/* Logout Button */}
            <button
                onClick={handleLogout}
                className="absolute top-4 right-4 px-4 py-2 bg-red-500 text-white rounded shadow hover:bg-red-600 transition-all z-20"
            >
                <i className="ri-logout-box-r-line mr-2"></i>Logout
            </button>
            <div className="projects flex flex-wrap gap-6 justify-center mt-8">
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="project p-6 border-2 border-blue-300 bg-white rounded-xl shadow-md hover:shadow-lg hover:bg-blue-50 transition-all text-lg font-semibold flex flex-col items-center justify-center min-w-56 min-h-32">
                    <span>New Project</span>
                    <i className="ri-add-circle-line text-3xl mt-2 text-blue-500"></i>
                </button>

                {
                    projects.map((project) => (
                        <div key={project._id} className="project flex flex-col gap-2 p-6 border-2 border-purple-200 rounded-xl min-w-56 bg-white hover:bg-purple-50 shadow-md hover:shadow-lg transition-all relative">
                            <h2 className='font-semibold text-xl text-purple-700'>{project.name}</h2>

                            <div className="flex gap-2 items-center">
                                <p> <small> <i className="ri-user-line"></i> Collaborators</small> :</p>
                                <span className="font-bold text-purple-500">{project.users.length}</span>
                            </div>

                            <div className="actions flex gap-2 mt-2">
                                <button 
                                    onClick={() => navigate(`/project`, { state: { project } })}
                                    className="open-btn flex-grow py-1 bg-blue-500 text-white rounded shadow hover:bg-blue-600 transition-all">
                                    <i className="ri-folder-open-line mr-1"></i>Open
                                </button>
                                <button 
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        openEditModal(project);
                                    }}
                                    className="edit-btn p-1 bg-gray-200 rounded hover:bg-gray-300">
                                    <i className="ri-edit-line"></i>
                                </button>
                                <button 
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        deleteProject(project._id);
                                    }}
                                    className="delete-btn p-1 bg-red-100 rounded hover:bg-red-200 text-red-600">
                                    <i className="ri-delete-bin-line"></i>
                                </button>
                            </div>
                        </div>
                    ))
                }


            </div>

            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-md shadow-md w-1/3">
                        <h2 className="text-xl mb-4">Create New Project</h2>
                        <form onSubmit={createProject}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700">Project Name</label>
                                <input
                                    onChange={(e) => setProjectName(e.target.value)}
                                    value={projectName}
                                    type="text" className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
                            </div>
                            <div className="flex justify-end">
                                <button type="button" className="mr-2 px-4 py-2 bg-gray-300 rounded-md" onClick={() => setIsModalOpen(false)}>Cancel</button>
                                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded-md">Create</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isEditModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-md shadow-md w-1/3">
                        <h2 className="text-xl mb-4">Edit Project</h2>
                        <form onSubmit={updateProject}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700">Project Name</label>
                                <input
                                    onChange={(e) => setProjectName(e.target.value)}
                                    value={projectName}
                                    type="text" className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
                            </div>
                            <div className="flex justify-end">
                                <button type="button" className="mr-2 px-4 py-2 bg-gray-300 rounded-md" onClick={() => {
                                    setIsEditModalOpen(false);
                                    setSelectedProject(null);
                                    setProjectName('');
                                }}>Cancel</button>
                                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded-md">Update</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}


        </main>
    )
}

export default Home