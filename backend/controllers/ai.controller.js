import * as ai from '../services/ai.service.js';

export const getResult = async (req, res) => {
    try {
        // Get prompt from either query params or request body
        const prompt = req.query.prompt || req.body.prompt;
        
        if (!prompt) {
            return res.status(400).json({ 
                error: "Prompt is required." 
            });
        }

        const result = await ai.generateResult(prompt);
        res.send(result);
    } catch (error) {
        res.status(500).json({  
            error: error.message || "Failed to generate AI response" 
        });
    }
}