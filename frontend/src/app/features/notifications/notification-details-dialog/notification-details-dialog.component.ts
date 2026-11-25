import {
  Component,
  Inject
} from '@angular/core';

import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogTitle,
  MatDialogClose
} from '@angular/material/dialog';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  NotificationResponse
} from '../models/notification-response.model';

@Component({
  selector: 'app-notification-detail-dialog',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    MatDialogClose
  ],
  templateUrl: './notification-detail-dialog.component.html',
  styleUrl: './notification-detail-dialog.component.scss'
})
export class NotificationDetailDialogComponent {

    constructor(
        @Inject(MAT_DIALOG_DATA)
        public data: NotificationResponse
    ) { }

}