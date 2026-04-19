import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  recoveryForm: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.recoveryForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]]
    });
  }

  get correoCtrl() {
    return this.recoveryForm.get('correo')!;
  }

  onSubmit(): void {
    if (this.recoveryForm.invalid) {
      this.recoveryForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.successMessage = '';
    this.errorMessage = '';

    const { correo } = this.recoveryForm.value;

    this.authService.forgotPassword(correo).subscribe({
      next: response => {
        this.isLoading = false;
        this.successMessage = response.mensaje || 'Revisa tu correo para continuar con la recuperacion.';
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;

        if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend este corriendo.';
          return;
        }

        this.errorMessage = error.error?.message || 'No fue posible generar la recuperacion de contrasena.';
      }
    });
  }
}
