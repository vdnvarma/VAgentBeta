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

export const executeCode = async ({ code, language, input = '' }) => {
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
        const child = exec(config.command, (error, stdout, stderr) => {
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

        // Provide input to the program
        if (input) {
            child.stdin.write(input);
            child.stdin.end();
        }
    });
};

const wss = new WebSocketServer({ port: 8081 }); // WebSocket server on port 8081

wss.on('connection', (ws) => {
    console.log('Client connected');

    let childProcess = null;

    ws.on('message', (message) => {
        const data = JSON.parse(message);

        if (data.type === 'start') {
            const { code, language } = data;

            console.log(`Executing code in ${language}:`, code);

            const languageConfig = {
                javascript: { extension: 'js', command: 'node' },
                python: { extension: 'py', command: 'python3' },
                java: { extension: 'java', command: `javac ${os.tmpdir()}/tempCode.java && java -cp ${os.tmpdir()} tempCode` },
                c: { extension: 'c', command: `gcc ${os.tmpdir()}/tempCode.c -o ${os.tmpdir()}/tempCode && ${os.tmpdir()}/tempCode` },
                cpp: { extension: 'cpp', command: `g++ ${os.tmpdir()}/tempCode.cpp -o ${os.tmpdir()}/tempCode && ${os.tmpdir()}/tempCode` },
            };

            const config = languageConfig[language.toLowerCase()];
            if (!config) {
                ws.send(JSON.stringify({ type: 'error', message: `Unsupported language: ${language}` }));
                return;
            }

            const tempFile = `${os.tmpdir()}/tempCode.${config.extension}`;

            try {
                fs.writeFileSync(tempFile, code);
                console.log(`Code written to temporary file: ${tempFile}`);
            } catch (err) {
                console.error('Error writing to temp file:', err);
                ws.send(JSON.stringify({ type: 'error', message: 'Failed to write code to temp file.' }));
                return;
            }

            childProcess = exec(config.command);

            childProcess.stdout.on('data', (data) => {
                ws.send(JSON.stringify({ type: 'output', message: data.toString() }));
            });

            childProcess.stderr.on('data', (data) => {
                ws.send(JSON.stringify({ type: 'error', message: data.toString() }));
            });

            childProcess.on('close', (code) => {
                ws.send(JSON.stringify({ type: 'close', message: `Process exited with code ${code}` }));
                childProcess = null;
            });
        } else if (data.type === 'input' && childProcess) {
            childProcess.stdin.write(data.input + '\n');
        }
    });

    ws.on('close', () => {
        console.log('Client disconnected');
        if (childProcess) {
            childProcess.kill();
        }
    });
});