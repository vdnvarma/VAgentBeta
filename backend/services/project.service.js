import projectModel from '../models/project.model.js';
import mongoose from 'mongoose';
import { exec } from 'child_process';
import fs from 'fs';
import os from 'os';
import { execSync } from 'child_process';


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

export const executeCode = async ({ code, language }) => {
    if (!code) {
        throw new Error('No code provided.');
    }

    if (!language) {
        throw new Error('No language specified.');
    }

    console.log(`Executing code in ${language}:`, code); // Debugging log

    const languageConfig = {
        javascript: { extension: 'js', command: 'node' },
        python: { extension: 'py', command: 'python3' },
        java: { extension: 'java', command: `javac ${os.tmpdir()}/tempCode.java && java -cp ${os.tmpdir()} tempCode` },
        c: { extension: 'c', command: `gcc ${os.tmpdir()}/tempCode.c -o ${os.tmpdir()}/tempCode && ${os.tmpdir()}/tempCode` },
        cpp: { extension: 'cpp', command: `g++ ${os.tmpdir()}/tempCode.cpp -o ${os.tmpdir()}/tempCode && ${os.tmpdir()}/tempCode` },
    };

    const config = languageConfig[language.toLowerCase()];
    if (!config) {
        throw new Error(`Unsupported language: ${language}`);
    }

    const tempFile = `${os.tmpdir()}/tempCode.${config.extension}`;

    try {
        // Save the code to a temporary file
        fs.writeFileSync(tempFile, code);
        console.log(`Code written to temporary file: ${tempFile}`);
    } catch (err) {
        console.error('Error writing to temp file:', err); // Log file write errors
        throw new Error('Failed to write code to temp file.');
    }

    // Execute the code using the appropriate command
    return new Promise((resolve, reject) => {
        exec(config.command, (error, stdout, stderr) => {
            try {
                // Delete the temporary source file
                if (fs.existsSync(tempFile)) {
                    fs.unlinkSync(tempFile);
                }

                // Delete the compiled binary for C/C++
                const compiledBinary = `${os.tmpdir()}/tempCode`;
                if (fs.existsSync(compiledBinary)) {
                    fs.unlinkSync(compiledBinary);
                }
            } catch (err) {
                console.error('Error deleting temp file:', err); // Log file delete errors
            }

            if (error) {
                console.error('Execution error:', stderr || error.message); // Log execution errors
                return reject(stderr || error.message);
            }

            console.log('Execution output:', stdout); // Debugging log
            resolve(stdout);
        });
    });
};