import projectModel from '../models/project.model.js';
import mongoose from 'mongoose';
import { exec } from 'child_process';
import fs from 'fs';
import os from 'os';
import { WebSocketServer } from 'ws';

export const createProject = async ({
    name, userId
}) => {
    if (!name) {
        throw new Error('Name is required')
    }
    if (!userId) {
        throw new Error('UserId is required')
    }

    let project;
    try {
        project = await projectModel.create({
            name,
            users: [ userId ]
        });
    } catch (error) {
        if (error.code === 11000) {
            throw new Error('Project name already exists');
        }
        throw error;
    }

    return project;

}


export const getAllProjectByUserId = async ({ userId }) => {
    if (!userId) {
        throw new Error('UserId is required')
    }

    const allUserProjects = await projectModel.find({
        users: userId
    })

    return allUserProjects
}

export const addUsersToProject = async ({ projectId, users, userId }) => {

    if (!projectId) {
        throw new Error("projectId is required")
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId")
    }

    if (!users) {
        throw new Error("users are required")
    }

    if (!Array.isArray(users) || users.some(userId => !mongoose.Types.ObjectId.isValid(userId))) {
        throw new Error("Invalid userId(s) in users array")
    }

    if (!userId) {
        throw new Error("userId is required")
    }

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        throw new Error("Invalid userId")
    }


    const project = await projectModel.findOne({
        _id: projectId,
        users: userId
    })

    console.log(project)

    if (!project) {
        throw new Error("User not belong to this project")
    }

    const updatedProject = await projectModel.findOneAndUpdate({
        _id: projectId
    }, {
        $addToSet: {
            users: {
                $each: users
            }
        }
    }, {
        new: true
    })

    return updatedProject



}

export const getProjectById = async ({ projectId }) => {
    if (!projectId) {
        throw new Error("projectId is required")
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId")
    }

    const project = await projectModel.findOne({
        _id: projectId
    }).populate('users')

    return project;
}

export const updateFileTree = async ({ projectId, fileTree }) => {
    if (!projectId) {
        throw new Error("projectId is required")
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId")
    }

    if (!fileTree) {
        throw new Error("fileTree is required")
    }

    const project = await projectModel.findOneAndUpdate({
        _id: projectId
    }, {
        fileTree
    }, {
        new: true
    })

    return project;
}

export const deleteProject = async ({ projectId, userId }) => {
    if (!projectId) {
        throw new Error("projectId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId");
    }

    if (!userId) {
        throw new Error("userId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        throw new Error("Invalid userId");
    }

    // Check if user is the creator (first user in the users array)
    const project = await projectModel.findOne({ _id: projectId });
    
    if (!project) {
        throw new Error("Project not found");
    }

    // Ensure the user is the creator (first user in the array)
    if (project.users[0].toString() !== userId.toString()) {
        throw new Error("Only the project creator can delete the project");
    }

    const deletedProject = await projectModel.findByIdAndDelete(projectId);
    
    return deletedProject;
}

export const updateProjectName = async ({ projectId, name, userId }) => {
    if (!projectId) {
        throw new Error("projectId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId");
    }

    if (!name) {
        throw new Error("name is required");
    }

    if (!userId) {
        throw new Error("userId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        throw new Error("Invalid userId");
    }

    // Check if user is the creator (first user in the users array)
    const project = await projectModel.findOne({ _id: projectId });
    
    if (!project) {
        throw new Error("Project not found");
    }

    // Ensure the user is the creator (first user in the array)
    if (project.users[0].toString() !== userId.toString()) {
        throw new Error("Only the project creator can update the project name");
    }

    try {
        const updatedProject = await projectModel.findOneAndUpdate(
            { _id: projectId },
            { name },
            { new: true }
        );

        return updatedProject;
    } catch (error) {
        if (error.code === 11000) {
            throw new Error('Project name already exists');
        }
        throw error;
    }
}

export const removeUserFromProject = async ({ projectId, userToRemove, userId }) => {
    if (!projectId) {
        throw new Error("projectId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId");
    }

    if (!userToRemove) {
        throw new Error("userToRemove is required");
    }

    if (!mongoose.Types.ObjectId.isValid(userToRemove)) {
        throw new Error("Invalid userToRemove");
    }

    if (!userId) {
        throw new Error("userId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        throw new Error("Invalid userId");
    }

    // Check if the requesting user is the creator
    const project = await projectModel.findOne({ _id: projectId });
    
    if (!project) {
        throw new Error("Project not found");
    }

    // Check if user is the creator (first user in the array)
    if (project.users[0].toString() !== userId.toString()) {
        throw new Error("Only the project creator can remove collaborators");
    }

    // Cannot remove the creator (first user)
    if (project.users[0].toString() === userToRemove.toString()) {
        throw new Error("Cannot remove the project creator");
    }

    const updatedProject = await projectModel.findOneAndUpdate(
        { _id: projectId },
        { $pull: { users: userToRemove } },
        { new: true }
    ).populate('users');

    return updatedProject;
}

export const leaveProject = async ({ projectId, userId }) => {
    if (!projectId) {
        throw new Error("projectId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
        throw new Error("Invalid projectId");
    }

    if (!userId) {
        throw new Error("userId is required");
    }

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        throw new Error("Invalid userId");
    }

    // Check if the project exists
    const project = await projectModel.findOne({ _id: projectId });
    
    if (!project) {
        throw new Error("Project not found");
    }

    // Check if the user is actually in the project
    if (!project.users.includes(userId)) {
        throw new Error("User is not a member of this project");
    }

    // Cannot leave if the user is the creator (first user)
    if (project.users[0].toString() === userId.toString()) {
        throw new Error("The project creator cannot leave the project");
    }

    const updatedProject = await projectModel.findOneAndUpdate(
        { _id: projectId },
        { $pull: { users: userId } },
        { new: true }
    ).populate('users');

    return updatedProject;
}