import { Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import { Board } from '../interface/board';
import { BoardService } from '../services/board.service';
import { SigninService } from '../services/singin.service';
import Swal from 'sweetalert2';
import { Task } from '../interface/task';
import { NotificationService } from '../services/notification.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-boards',
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.css']
})
export class BoardsComponent implements OnInit {
  @Output() boardCreated: EventEmitter<void> = new EventEmitter<void>();
  @Input() userId: string | null = null; // Input to receive the userId
  boards: Board[] = [];
  showCreateBoardForm = false;
  isHandset$: Observable<boolean>;
  selectedBoard: Board | null = null;
  showTasks: boolean[] = []; 
  boardTasks: { [boardId: string]: Task[] } = {};
  newBoard: Board = {
    boardId: '',
    boardTitle: '',
    userId: '',
    tasks: [],
    emailId: ''
    
  };

  title = 'material-responsive-sidenav';
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
  isMobile= true;
  isCollapsed = true;

  constructor(
    private boardService: BoardService,
    private signinService: SigninService,
    private notificationService: NotificationService,
    private breakpointObserver: BreakpointObserver
  ) {
    this.isHandset$ = this.breakpointObserver.observe('(max-width: 768px)')
      .pipe(
        map(result => result.matches)
      );
  }
  ngOnInit(): void {
    console.log('Boards component initialized');
    const userEmail = this.signinService.getUserEmail();
    console.log('Retrieved User Email:', userEmail);
    if (userEmail) {
        this.boardService.getBoardsByAssigneeEmail(userEmail).subscribe(
            (boards) => {
                // Filter out the boards where the current user is the assignee based on tasks
                const assignedBoards = boards.filter(board => this.isAssigneeInTasks(userEmail, board?.tasks));
                
                // Fetch the boards created by the current user
                this.boardService.getBoardsByUserEmail(userEmail).subscribe(
                    (createdBoards) => {
                        // Merge the boards created by the user with the assigned boards
                        const mergedBoards = this.mergeBoards(assignedBoards, createdBoards);
                        this.boards = mergedBoards;
                        mergedBoards.forEach(board => this.fetchTasksForBoard(board.boardId));
                    },
                    (error) => {
                        console.error('Error getting boards by user email: ', error);
                    }
                );
            },
            (error) => {
                console.error('Error getting boards by assignee email: ', error);
            }
        );
    } else {
        console.error('User Email not found in signinService');
    }
    this.breakpointObserver.observe(['(max-width: 800px)']).subscribe((screenSize) => {
      if(screenSize.matches){
        this.isMobile = true;
      } else {
        this.isMobile = false;
      }
    });
}

mergeBoards(assignedBoards: Board[], createdBoards: Board[]): Board[] {
    // Merge two arrays of boards removing duplicates based on boardId
    const mergedBoardsMap = new Map<string, Board>();
    assignedBoards.forEach(board => mergedBoardsMap.set(board.boardId, board));
    createdBoards.forEach(board => mergedBoardsMap.set(board.boardId, board));
    return Array.from(mergedBoardsMap.values());
}

isAssigneeInTasks(assigneeEmail: string, tasks: Task[] | undefined): boolean {
    // Check if tasks is defined and assignee's email is present in any of the tasks
    return tasks?.some(task => task.assigneeEmail === assigneeEmail) || false;
}



  fetchTasksForBoard(boardId: string): void {
    this.boardService.getTasksOfBoard(boardId).subscribe(
      (tasks) => {
        this.boardTasks[boardId] = tasks;
      },
      (error) => {
        console.error('Error fetching tasks for boardId:', boardId, error);
      }
    );
  }
  
  getBoards(userId: string): void {
    this.boardService.getBoardsByUserId(userId).subscribe(
      (boards) => {
        this.boards = boards;
      },
      (error) => {
        console.error('Error getting boards: ', error);
      }
    );
  }

  selectBoard(boardId: string): void {
    if (boardId) {
        // Reset selectedBoard to null
        this.selectedBoard = null;

        const selectedBoard = this.boards.find((board) => board.boardId === boardId);
        if (selectedBoard) {
            // Fetch tasks for the selected board
            console.log('Fetching tasks for boardId:', boardId);
            this.boardService.getTasksOfBoard(boardId).subscribe(
                (tasks) => {
                    // Store fetched tasks in the boardTasks dictionary
                    this.boardTasks[boardId] = tasks;
                    // Update the selected board with the fetched tasks
                    selectedBoard.tasks = tasks;
                    this.selectedBoard = selectedBoard; // Set the selected board after fetching tasks
                    console.log('Fetched tasks for boardId:', boardId, tasks);
                },
                (error) => {
                    console.error('Error fetching tasks for boardId:', boardId, error);
                }
            );
        } else {
            console.error('Cannot fetch tasks for board with null boardId');
        }
    }
}

toggleMenu() {
  if(this.isMobile){
    this.sidenav.toggle();
    this.isCollapsed = false;
  } else {
    this.sidenav.open();
    this.isCollapsed = !this.isCollapsed;
  }
}
  
addBoard(event: Event): void {
  event.preventDefault(); // Default form submission rokna
  console.log('Adding board:', this.newBoard);
  const userId = this.signinService.getUserId();
  const emailId = this.signinService.getUserEmail(); // EmailId retrieve karna
  if (userId && emailId) { // Agar userId aur emailId dono available hain
    this.newBoard.userId = userId; // Naye board ke liye userId set karna
    this.newBoard.emailId = emailId; // Naye board ke liye emailId set karna
    if (this.validateBoard()) { // Board ko validate karna
      // Board service ka upyog karke board create karne ka HTTP request bhejna
      this.boardService.createBoard(userId, this.newBoard).subscribe(
        newBoard => {
          console.log('Board created:', newBoard);
          this.boards.push(newBoard);
          this.showCreateBoardForm = false;
          this.newBoard = {
            boardId: '', // Backend par generate hoga
            boardTitle: '',
            userId: '', // Backend par generate hoga
            tasks: [],
            emailId: ''
          };
          localStorage.setItem('boardId', newBoard.boardId);
          this.notificationService.addNotification(`New board created: ${newBoard.boardTitle}`);
        },
        error => {
          console.error('Error creating board: ', error);
          // Error handle karna
          Swal.fire('Error!', 'Failed to create board. Please try again later.', 'error');
        }
      );
    }
  } else {
    console.log('User ID or Email ID not found in local storage'); // Agar userId ya emailId available nahi hain, toh error log karna
  }
}

validateBoard(): boolean {
  if (!this.newBoard.boardTitle.trim()) {
    console.log('Error!', 'Board Title cannot be empty.', 'error');
    return false;
  }
  return true;
}


  toggleCreateBoardForm(): void {
    this.showCreateBoardForm = !this.showCreateBoardForm;
  }
  
  toggleTasks(index: number) {
    this.showTasks[index] = !this.showTasks[index];
  }
  updateBoard(index: number): void {
    const board = this.boards[index];
    Swal.fire({
      title: 'Enter new board title:',
      input: 'text',
      inputValue: board.boardTitle,
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Update'
    }).then((result) => {
      if (result.isConfirmed) {
        const editedBoardTitle = result.value;
        if (editedBoardTitle.trim() !== '') {
          // Update the board title locally
          board.boardTitle = editedBoardTitle;
          console.log('Updated board object:', board); // Debugging: Log the updated board object
  
          // Update the board on the backend
          this.boardService.updateBoard(board).subscribe(
            (updated) => {
              console.log('Board updated successfully:', updated);
              // Find the index of the updated board in the boards array
              const updatedBoardIndex = this.boards.findIndex(b => b.boardId === updated.boardId);
              if (updatedBoardIndex !== -1) {
                // Update the board in the local array
                this.boards[updatedBoardIndex] = updated;
              }
              Swal.fire('Updated!', 'Your board has been updated.', 'success');
              
            },
            (error) => {
              console.error('Error updating board:', error);
              Swal.fire('Error!', 'Failed to update the board.', 'error');
            }
          );
        } else {
          Swal.fire('Error!', 'Board title cannot be empty.', 'error');
        }
      }
    });
  }

  
  
  
  deleteBoard(index: number): void {
    const board = this.boards[index];
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this board!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        const boardIdToDelete = board.boardId;
        this.boardService.deleteBoard(boardIdToDelete).subscribe(
          (response) => {
            console.log(response);
            this.boards.splice(index, 1);

            Swal.fire('Deleted!', 'Your board has been deleted.', 'success').then(() => {
              // Reload the window after successful deletion
              window.location.reload();
            });
            
      
          },
          (error) => {
            console.error('Error deleting board:', error);
            Swal.fire('Error!', 'Failed to delete the board.', 'error');
          }
        );
      }
    });
  }
  
}