import {
  Injectable,
  signal
} from '@angular/core';

import {
  AppointmentResponse
} from '../models/appointment-response.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentStateService {

  readonly appointments =
    signal<AppointmentResponse[]>([]);

  readonly selectedAppointment =
    signal<AppointmentResponse | null>(
      null
    );

  findAppointment(
    appointmentId: string
  ): AppointmentResponse | undefined {

    return this.appointments()
      .find(
        appointment =>
          appointment.id === appointmentId
      );

  }

  selectAppointment(
    appointment: AppointmentResponse
  ): void {

    this.selectedAppointment
      .set(appointment);

  }

  clearSelectedAppointment(): void {

    this.selectedAppointment
      .set(null);

  }

}