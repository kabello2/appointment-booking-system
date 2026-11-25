import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { ServiceRequest } from '../models/service-request.model';

import { ServiceResponse } from '../models/service-response.model';
import { PageResponse } from '../../../core/models/page-response.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ServiceOfferingService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getServices(
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<ServiceResponse>> {

    return this.http.get<PageResponse<ServiceResponse>>(

      `${this.apiUrl}/services?page=${page}&size=${size}`

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