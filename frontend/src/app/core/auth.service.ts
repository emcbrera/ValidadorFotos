import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ForgotPasswordRequest } from '../schemas/request/forgot-password-request';
import { LoginRequest } from '../schemas/request/login-request';
import { ResetPasswordRequest } from '../schemas/request/reset-password-request';
import { AuthResponse } from '../schemas/response/auth-response';
import { ForgotPasswordResponse } from '../schemas/response/forgot-password-response';
import { ResetPasswordResponse } from '../schemas/response/reset-password-response';
import { User } from '../schemas/response/user-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = '/api/auth';
  private readonly SESSION_KEY = 'auth_user';
  private readonly TOKEN_KEY = 'auth_token';

  constructor(private http: HttpClient) {}

  login(identificador: string, password: string): Observable<AuthResponse> {
    const request: LoginRequest = { identificador, password };

    return this.http.post<AuthResponse>(`${this.API_URL}/login`, request)
      .pipe(
        tap(response => {
          if (response.token) {
            localStorage.setItem(this.TOKEN_KEY, response.token);
            const user: User = { 
              email: response.correo, 
              name: response.username, 
              role: response.rol 
            };
            localStorage.setItem(this.SESSION_KEY, JSON.stringify(user));
          }
        })
      );
  }

  forgotPassword(correo: string): Observable<ForgotPasswordResponse> {
    const request: ForgotPasswordRequest = { correo };
    return this.http.post<ForgotPasswordResponse>(`${this.API_URL}/forgot-password`, request);
  }

  resetPassword(token: string, nuevaPassword: string): Observable<ResetPasswordResponse> {
    const request: ResetPasswordRequest = { token, nuevaPassword };
    return this.http.post<ResetPasswordResponse>(`${this.API_URL}/reset-password`, request);
  }

  logout(): void {
    localStorage.removeItem(this.SESSION_KEY);
    localStorage.removeItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  getCurrentUser(): User | null {
    const data = localStorage.getItem(this.SESSION_KEY);
    return data ? JSON.parse(data) : null;
  }
}
