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

  setAppointments(
    appointments: AppointmentResponse[]
  ): void {

    this.appointments.set(
      appointments
    );

  }

  addAppointment(
    appointment: AppointmentResponse
  ): void {

    this.appointments.update(
      appointments => [
        appointment,
        ...appointments
      ]
    );

  }

  updateAppointment(
    updatedAppointment: AppointmentResponse
  ): void {

    this.appointments.update(
      appointments =>
        appointments.map(
          appointment =>
            appointment.id === updatedAppointment.id
              ? updatedAppointment
              : appointment
        )
    );

    if (
      this.selectedAppointment()?.id
      === updatedAppointment.id
    ) {

      this.selectedAppointment.set(
        updatedAppointment
      );

    }

  }

  removeAppointment(
    appointmentId: string
  ): void {

    this.appointments.update(
      appointments =>
        appointments.filter(
          appointment =>
            appointment.id !== appointmentId
        )
    );

  }


}