import { Router } from 'express';
import { body } from 'express-validator';
import * as projectController from '../controllers/project.controller.js';
import * as authMiddleWare from '../middleware/auth.middleware.js';

const router = Router();


router.post('/create',
    authMiddleWare.authUser,
    body('name').isString().withMessage('Name is required'),
    projectController.createProject
)

router.get('/all',
    authMiddleWare.authUser,
    projectController.getAllProject
)

router.put('/add-user',
    authMiddleWare.authUser,
    body('projectId').isString().withMessage('Project ID is required'),
    body('users').isArray({ min: 1 }).withMessage('Users must be an array of strings').bail()
        .custom((users) => users.every(user => typeof user === 'string')).withMessage('Each user must be a string'),
    projectController.addUserToProject
)

router.get('/get-project/:projectId',
    authMiddleWare.authUser,
    projectController.getProjectById
)

router.put('/update-file-tree',
    authMiddleWare.authUser,
    body('projectId').isString().withMessage('Project ID is required'),
    body('fileTree').isObject().withMessage('File tree is required'),
    projectController.updateFileTree
)

router.delete('/delete/:projectId',
    authMiddleWare.authUser,
    projectController.deleteProject
)

router.put('/update-name',
    authMiddleWare.authUser,
    body('projectId').isString().withMessage('Project ID is required'),
    body('name').isString().withMessage('Name is required'),
    projectController.updateProjectName
)

router.put('/remove-user',
    authMiddleWare.authUser,
    body('projectId').isString().withMessage('Project ID is required'),
    body('userToRemove').isString().withMessage('User ID to remove is required'),
    projectController.removeUserFromProject
)

router.put('/leave-project',
    authMiddleWare.authUser,
    body('projectId').isString().withMessage('Project ID is required'),
    projectController.leaveProject
)

export default router;