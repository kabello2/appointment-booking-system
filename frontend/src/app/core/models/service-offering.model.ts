export interface ServiceOffering {
  id: string;
  serviceCode: string;
  name: string;
  description?: string;
  durationMinutes: number;
  active: boolean;
}