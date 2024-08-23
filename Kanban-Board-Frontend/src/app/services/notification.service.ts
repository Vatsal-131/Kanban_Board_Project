import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly STORAGE_KEY = 'notifications';

  private notificationsSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
  public notifications$: Observable<string[]> = this.notificationsSubject.asObservable();

  constructor() {
    // Initialize notifications from localStorage
    const storedNotifications = localStorage.getItem(this.STORAGE_KEY);
    const initialNotifications = storedNotifications ? JSON.parse(storedNotifications) : [];
    this.notificationsSubject.next(initialNotifications);
  }

  addNotification(notification: string): void {
    const notifications = this.notificationsSubject.getValue();
    notifications.push(notification);
    this.updateLocalStorage(notifications);
  }

  Notification(notification: string): void {
    const notifications = this.notificationsSubject.getValue();
    notifications.push(notification);
    this.notificationsSubject.next(notifications);
  }
  removeNotification(index: number): void {
    const notifications = this.notificationsSubject.getValue();
    notifications.splice(index, 1);
    this.updateLocalStorage(notifications);
  }

  removeAllNotifications(): void {
    const notifications: string[] = [];
    this.updateLocalStorage(notifications);
  }
  
  private updateLocalStorage(notifications: string[]): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(notifications));
    this.notificationsSubject.next(notifications);
  }
}