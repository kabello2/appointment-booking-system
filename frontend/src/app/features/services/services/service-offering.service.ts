import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { ServiceRequest } from '../models/service-request.model';

import { ServiceResponse } from '../models/service-response.model';

@Injectable({
  providedIn: 'root'
})
export class ServiceOfferingService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getServices() {

    return this.http.get<ServiceResponse[]>(

      `${this.apiUrl}/services`

    );

  }

  createService(
    request: ServiceRequest
  ) {

    return this.http.post<ServiceResponse>(

      `${this.apiUrl}/services`,
      request

    );

  }

  deleteService(
    serviceId: string
  ) {

    return this.http.delete(

      `${this.apiUrl}/services/${serviceId}`

    );

  }

}