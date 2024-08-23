export interface Task {
  taskId: string;
  taskName: string;
  taskDescription: string;
  taskDeadline: string;
  assigneeEmail: string;
  initialState: string; // Add initialState field
  status: string;
}