import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DatosPersonaResponse {
  id?: number;
  usuarioId?: number;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  tipoDocumento: string;
  numeroDocumento: number;
  correo: string;
  genero: string;
  celular: string;
  foto?: string;
  fotoUrl?: string;
  estado?: string;
  mensaje?: string;
}

export interface DatosPersonaRequest {
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  tipoDocumento: string;
  numeroDocumento: number;
  correo: string;
  genero: string;
  celular: string;
}

@Injectable({
  providedIn: 'root'
})
export class EstudianteService {
  private readonly API_URL = '/api/estudiante/datos-personales';

  constructor(private http: HttpClient) {}

  obtenerDatosPersonales(): Observable<DatosPersonaResponse> {
    return this.http.get<DatosPersonaResponse>(this.API_URL);
  }

  registrarDatosPersonales(datos: any, foto: File): Observable<DatosPersonaResponse> {
    const formData = new FormData();
    formData.append('primerNombre', datos.primerNombre);
    if (datos.segundoNombre) formData.append('segundoNombre', datos.segundoNombre);
    formData.append('primerApellido', datos.primerApellido);
    if (datos.segundoApellido) formData.append('segundoApellido', datos.segundoApellido);
    formData.append('tipoDocumento', datos.tipoDocumento);
    formData.append('numeroDocumento', datos.numeroDocumento.toString());
    formData.append('correo', datos.correo);
    formData.append('genero', datos.genero);
    formData.append('celular', datos.celular);
    formData.append('foto', foto);

    return this.http.post<DatosPersonaResponse>(this.API_URL, formData);
  }

  actualizarDatosPersonales(datos: DatosPersonaRequest): Observable<DatosPersonaResponse> {
    return this.http.put<DatosPersonaResponse>(this.API_URL, datos);
  }

  actualizarFoto(foto: File): Observable<DatosPersonaResponse> {
    const formData = new FormData();
    formData.append('foto', foto);
    return this.http.put<DatosPersonaResponse>(`${this.API_URL}/foto`, formData);
  }
}
