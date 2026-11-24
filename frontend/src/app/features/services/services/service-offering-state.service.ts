import {
    Injectable,
    signal
} from '@angular/core';

import { ServiceResponse } from '../models/service-response.model'; 

@Injectable({
    providedIn: 'root'
})
export class ServiceOfferingStateService {

    readonly services =
        signal<ServiceResponse[]>([]);

    setServiceOffering(
        services: ServiceResponse[]
    ): void {
        this.services.set(
            services
        );
    };

    addServiceOffering(
        service: ServiceResponse
    ): void {

        this.services.update(
            services => [
                service,
                ...services
            ]
        );
    };


    removeServiceOffering(
        serviceId: string
    ): void {

        this.services.update(
            services =>
                services.filter(
                    service =>
                        service.id !== serviceId
                )
        );

    }
}