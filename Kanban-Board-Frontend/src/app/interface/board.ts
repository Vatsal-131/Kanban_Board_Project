import { Task } from './task';

export interface Board {
    emailId: string;
    boardId: string;
    boardTitle: string;
    userId: string;
    tasks?: Task[];
  }