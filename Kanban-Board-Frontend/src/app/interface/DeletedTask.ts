export class DeletedTask {
  taskId: string;
  taskName: string;
  boardId: string;
  userId: string;
  taskDescription: string;
  taskDeadline: string;
  assigneeEmail: string;
  initialState: string;


  constructor(
    taskId: string,
    taskName: string,
    boardId: string,
    userId: string,
    taskDescription: string,
    taskDeadline: string,

    assigneeEmail: string,
    initialState: string
    
  ) {
    this.taskId = taskId;
    this.taskName = taskName;
    this.boardId = boardId;
    this.userId = userId;
    this.taskDescription = taskDescription;
    this.taskDeadline = taskDeadline;
    this.assigneeEmail = assigneeEmail;
    this.initialState = initialState;

  }
}