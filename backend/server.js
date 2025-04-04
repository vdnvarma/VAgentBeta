import 'dotenv/config';
import http from 'http';
import app from './app.js';
import jwt from 'jsonwebtoken';
import { Server } from 'socket.io';
import mongoose from 'mongoose';
import projectModel from './models/project.model.js';
import { generateResult } from './services/ai.service.js';


const port = process.env.PORT || 3000;

const server = http.createServer(app);

const io = new Server(server,{
    cors: {
        origin: '*'
    }
});


io.use(async (socket,next) => {
    try{
        const token = socket.handshake.auth.token  || socket.handshake.headers.authorization?.split(' ')[1];
        const projectId = socket.handshake.query.projectId;

        if(!mongoose.Types.ObjectId.isValid(projectId)){
            return next(new Error("Invalid ProjectId"));
        }


        socket.project = await projectModel.findById(projectId);
        

        if(!token){
            return next(new Error('Authentication error'));
        }
        
        const decoded = jwt.verify(token, process.env.JWT_SECRET);

        if(!decoded){
            return next(new Error('Authentication error'));
        }

        socket.user = decoded;
        next();



    }catch(error){
        next(error);
    }
});


io.on('connection', socket => {

    const roomId = socket.project._id.toString();

    
    console.log('a user connected');
    
    socket.join(roomId);

    socket.on('project-message', async data => {
        const message = data.message;

        const aiIsPresentInMessage = message.includes('@ai');
        socket.broadcast.to(roomId).emit('project-message', data);

        if (aiIsPresentInMessage) {
            const prompt = message.replace('@ai', '').trim();

            if (prompt) {
                const result = await generateResult(prompt);

                io.to(roomId).emit('project-message', {
                    message: result,
                    sender: {
                        _id: 'ai',
                        email: 'AI'
                    }
                });
            }

            return;
        }
    });

    socket.on('disconnect', () => {
        console.log('user disconnected');
        socket.leave(roomId);
    });
});

server.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});