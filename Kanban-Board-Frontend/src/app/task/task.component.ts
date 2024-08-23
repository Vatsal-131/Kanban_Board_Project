import { Component, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { BoardService } from '../services/board.service';
import { Task } from '../interface/task';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  @Input() boardId: string | undefined;
  tasks: Task[] = [];
  taskName: string = '';
  taskDescription: string = '';
  taskDeadline: string = '';
  assigneeEmail: string = '';
  showCreateTaskForm: boolean = false;
  currentState: string = '';
  selectedTask: any;
  archivedColumnName: string = 'Archived';
  showAssigneeDetails: boolean = false;
  showDeadlineDetails: boolean = false;

  constructor(private http: HttpClient, private boardService: BoardService,
    private notificationService: NotificationService
    ) {}
  
  ngOnInit(): void {
    if (this.boardId) {
      this.fetchTasks(this.boardId);
    } else {
      console.error('Board ID not provided.');
    }
  }
  
 
  toggleAssigneeDetails() {
    this.showAssigneeDetails = !this.showAssigneeDetails;
  }

  toggleDeadlineDetails() {
    this.showDeadlineDetails = !this.showDeadlineDetails;
  }
  toggleCreateTaskForm(state: string) {
    this.currentState = state;
    this.showCreateTaskForm = !this.showCreateTaskForm;
  }
  minDate(): string {
    const today = new Date();
    return this.formatDate(today);
  }
  formatDate(date: Date): string {
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0'); // January is 0
    const yyyy = date.getFullYear();

    return `${yyyy}-${mm}-${dd}`;
  }
  

  fetchTasks(boardId: string): void {
    console.log('Fetching tasks for board ID:', boardId);
    this.http.get<any[]>(`http://localhost:33333/task/board/${boardId}`)
      .subscribe(
        (tasks) => {
          console.log('Tasks fetched:', tasks);
          // Filter out duplicates manually
          const uniqueTasks = this.removeDuplicateTasks(tasks);
          this.tasks = uniqueTasks;
        },
        (error) => {
          console.error('Error fetching tasks:', error);
          // Handle error appropriately (e.g., show error message)
        }
      );
  }
  
  removeDuplicateTasks(tasks: Task[]): Task[] {
    const uniqueTasks: Task[] = [];
    const taskIds: Set<string> = new Set();
    tasks.forEach(task => {
      if (!taskIds.has(task.taskId)) {
        uniqueTasks.push(task);
        taskIds.add(task.taskId);
      }
    });
    return uniqueTasks;
  }
  
  
  

  createTask(state: string, boardId: string) {
    if (this.taskName && this.taskName.trim() !== '' &&
        this.taskDescription && this.taskDescription.trim() !== '' &&
        this.taskDeadline && this.assigneeEmail) {

      const userId = localStorage.getItem('userId');

      const body = {
        taskName: this.taskName,
        boardId: boardId,
        userId: userId,
        taskDescription: this.taskDescription,
        taskDeadline: this.taskDeadline,
        assigneeEmail: this.assigneeEmail,
        initialState: state,
        columnName: state === 'archived' ? this.archivedColumnName : null
      };

      this.http.post('http://localhost:33333/task/create', body)
        .subscribe(
          (response: any) => {
            console.log('Task created successfully:', response);
            this.fetchTasks(boardId);
            this.resetForm();
            this.showCreateTaskForm = false;
            Swal.fire('Success!', 'Task created successfully!', 'success');

            // Extract assigneeEmail from the response
            const assignedTo = response.assigneeEmail || this.assigneeEmail;

            // Determine the notification message based on the assignee
            if (userId === assignedTo) {
              // If the creator is also the assignee
              this.notificationService.addNotification(`You have created a new task.`);
            } else {
              // If the creator assigns the task to someone else
              this.notificationService.addNotification(`You have assigned a new task to ${assignedTo}.`);
            }

          },
          (error) => {
            console.error('Error creating task:', error);
            if (error.status === 400 && error.error === "EmailID you provided is not registered") {
              // Handle specific error message for status code 400
              Swal.fire('Error!', 'Email ID you provided is not registered.', 'error');
              this.showCreateTaskForm = false;
            } else if (error.status === 400 && error.error === "Assignee email does not exist in the user database") {
              // Handle specific error message for status code 400
              Swal.fire('Error!', 'The assignee email You provided is not registered..', 'error');
              this.showCreateTaskForm = false;
            } else {
              Swal.fire('Error!', 'Failed to create task. Please try again later.', 'error').then(() => {
                // Close the modal here
                this.showCreateTaskForm = false;
              }); 
            }
          }
        );
    } else {
      console.log('Error!', 'Please provide all the required task details.', 'error');
    }
}
   
  deleteTask(taskId: string) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this task!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(`http://localhost:33333/task/${taskId}`, { responseType: 'text' })
          .subscribe(
            () => {
              console.log('Task deleted successfully');
              // Remove the deleted task from the local tasks array
              this.tasks = this.tasks.filter(task => task.taskId !== taskId);
              Swal.fire('Deleted!', 'Your task has been deleted.', 'success');
            },
            (error) => {
              console.error('Error deleting task:', error);
              Swal.fire('Error!', 'Failed to delete the task.', 'error');
            }
          );
      } else {
        Swal.fire('Cancelled', 'Your task is safe :)', 'info');
      }
    });
  }
  
  

  private resetForm() {
    this.taskName = '';
    this.taskDescription = '';
    this.taskDeadline = '';
    
    this.assigneeEmail='';
  }

  // Implement drag and drop methods
  allowDrop(event: DragEvent) {
    event.preventDefault();
  }

  drag(task: Task, event: DragEvent) {
    if (event.dataTransfer) {
      event.dataTransfer.setData('text/plain', task.taskId);
    }
  }
  
  drop(state: string, event: DragEvent) {
    event.preventDefault();
    console.log('Dropping task into state:', state); // Debugging statement
    const taskId = event.dataTransfer?.getData('text/plain');
    console.log('Task ID:', taskId); // Debugging statement
    if (taskId) {
      // Make PUT request to move the task
      this.http.put(`http://localhost:33333/task/${taskId}/move?status=${state}`, {})
        .subscribe(
          (response) => {
            console.log('Task moved successfully:', response);
            // Find the moved task in the local array
            const movedTaskIndex = this.tasks.findIndex(task => task.taskId === taskId);
            if (movedTaskIndex !== -1) {
              // Update the task's state to match the new state
              console.log('Updating task state to:', state); // Debugging statement
              this.tasks[movedTaskIndex].initialState = state;
              // If the selected task is the moved task, update its details view
              if (this.selectedTask && this.selectedTask.taskId === taskId) {
                this.selectedTask.initialState = state;
              }
              console.log('Updated tasks array:', this.tasks); // Debugging statement
            }
          },
          (error) => {
            console.error('Error moving task:', error);
          }
        );
    }
  }
  
  


  
  editTask(task: any) {
    this.selectedTask = task;
    this.taskName = task.taskName;
    this.taskDescription = task.taskDescription;
    this.taskDeadline = task.taskDeadline;
    
    this.assigneeEmail = task.assigneeEmail;
  }

  updateTask(task: any) {
    this.http.put(`http://localhost:33333/task/${task.taskId}`, task).subscribe(
      () => {
        this.fetchTasks(this.boardId!); // Pass boardId when fetching tasks
        this.cancelCreateTask();
        this.cancelEditTask();
      },
      (error) => {
        console.error('Error updating task:', error);
        if (error.status === 400 && error.error === "EmailID you provided is not registered") {
          // Handle specific error message for status code 400
          Swal.fire('Error!', 'Email ID you provided is not registered.', 'error');
          this.cancelCreateTask(); // Hide the form after handling the error
          this.cancelEditTask(); // Hide the form after handling the error
        } else {
          Swal.fire('Error!', 'Failed to update task. Please try again later.', 'error');
        }
      }
    );
  }
  
  

  cancelCreateTask() {
    this.showCreateTaskForm = false;
    this.resetForm();
    this.selectedTask = null;
  }

  cancelEditTask() {
    this.selectedTask = null;
  }
 

  getTasksByState(state: string): any[] {
    return this.tasks.filter((task: any) => task.initialState === state);
  }

  completeTask(task: any) {
    let state = '';
    // Determine the new status based on the current state of the task
    if (task.initialState === 'todo') {
      state = 'inprogress';
    } else if (task.initialState === 'inprogress') {
      state = 'completed';
    } else if (task.initialState === 'completed') {
      state = 'archived';
    }
    
    // Make an HTTP PUT request to the backend endpoint
    this.http.put(`http://localhost:33333/task/${task.taskId}/move?status=${state}`, {})
      .subscribe(
        (response) => {
          console.log('Task moved successfully:', response);
          this.notificationService.addNotification(' task moved');
          // Optionally, update the task locally in your component's data
          // For example, if you have an array of tasks, update the task's state accordingly
          this.fetchTasks(this.boardId!); // Refresh tasks list after successful move
        },
        (error) => {
          console.error('Error moving task:', error);
          // Handle error scenarios
        }
      );
  }  
}