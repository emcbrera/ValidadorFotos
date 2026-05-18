import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface FotoPendienteResponse {
  datosPersonaId: number;
  usuarioId: number;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  tipoDocumento: string;
  numeroDocumento: number;
  correo: string;
  celular: string;
  foto: string;
  fotoUrl: string;
  observacionRevision?: string;
  estado: string;
}

export interface RechazarFotoRequest {
  observacion: string;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly API_URL = '/api/admin/fotos';

  constructor(private http: HttpClient) {}

  listarFotosPendientes(): Observable<FotoPendienteResponse[]> {
    return this.http.get<FotoPendienteResponse[]>(`${this.API_URL}/pendientes`);
  }

  obtenerDetalleFotoPendiente(datosPersonaId: number): Observable<FotoPendienteResponse> {
    return this.http.get<FotoPendienteResponse>(`${this.API_URL}/pendientes/${datosPersonaId}`);
  }

  aprobarFoto(datosPersonaId: number): Observable<FotoPendienteResponse> {
    return this.http.put<FotoPendienteResponse>(`${this.API_URL}/${datosPersonaId}/aprobar`, {});
  }

  rechazarFoto(datosPersonaId: number, observacion: string): Observable<FotoPendienteResponse> {
    const request: RechazarFotoRequest = { observacion };
    return this.http.put<FotoPendienteResponse>(`${this.API_URL}/${datosPersonaId}/rechazar`, request);
  }
}
