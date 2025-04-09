import express from 'express';
import { exec } from 'child_process';
import fs from 'fs';
import { Router } from "express";

const router = Router();


router.post('/execute-code', (req, res) => {
    const { code } = req.body;

    if (!code) {
        return res.status(400).json({ output: 'No code provided.' });
    }

    // Save the code to a temporary file
    const tempFile = './tempCode.js';
    fs.writeFileSync(tempFile, code);

    // Execute the code using Node.js
    exec(`node ${tempFile}`, (error, stdout, stderr) => {
        fs.unlinkSync(tempFile); // Clean up the temporary file

        if (error) {
            return res.status(500).json({ output: stderr || error.message });
        }

        res.json({ output: stdout });
    });
});

export default router;