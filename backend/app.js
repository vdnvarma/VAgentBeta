import express from 'express';
import morgan from 'morgan';
import connect from './db/db.js';
import userRoutes from './routes/user.routes.js';
import projectRoutes from './routes/project.routes.js';
import aiRoutes from './routes/ai.routes.js';
import cookieParser from 'cookie-parser';
import cors from 'cors';

connect();

const app = express();

const corsOptions = {
    origin: ['https://vagent.onrender.com'], // Add your frontend URL here
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'], // Specify allowed methods
    credentials: true, // Allow credentials (cookies, authorization headers, etc.)
};

app.use(cors(corsOptions));
app.use(morgan('dev'));
app.use(express.json()); // Middleware to parse JSON requests
app.use(express.urlencoded({ extended: true}));
app.use(cookieParser());

app.use('/users', userRoutes);
app.use('/projects', projectRoutes); // Register the project routes
app.use('/ai', aiRoutes);

app.get('/', (req,res) =>{
    res.send("Hello World");
});

console.log(app._router.stack); // Logs all registered routes

// Start the server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});

export default app;