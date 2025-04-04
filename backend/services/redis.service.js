import Redis from "ioredis";


const redisCliet = new Redis({
    host: process.env.REDIS_HOST,
    port: process.env.REDIS_PORT,
    password: process.env.REDIS_PASSWORD
});

redisCliet.on('connect', () => {
    console.log("Redis Connected");
});

export default redisCliet;