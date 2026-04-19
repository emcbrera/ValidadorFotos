import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  isLoading = false;
  showPassword = false;
  showConfirmPassword = false;
  successMessage = '';
  errorMessage = '';
  token = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {
    this.resetForm = this.fb.group(
      {
        nuevaPassword: ['', [Validators.required, Validators.minLength(8)]],
        confirmarPassword: ['', [Validators.required]]
      },
      { validators: this.passwordsMatchValidator }
    );
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';

    if (!this.token) {
      this.errorMessage = 'El enlace de recuperacion no contiene un token valido.';
      this.resetForm.disable();
    }
  }

  get nuevaPasswordCtrl() {
    return this.resetForm.get('nuevaPassword')!;
  }

  get confirmarPasswordCtrl() {
    return this.resetForm.get('confirmarPassword')!;
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onSubmit(): void {
    if (this.resetForm.invalid || !this.token) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.successMessage = '';
    this.errorMessage = '';

    const { nuevaPassword } = this.resetForm.value;

    this.authService.resetPassword(this.token, nuevaPassword).subscribe({
      next: response => {
        this.isLoading = false;
        this.successMessage = response.mensaje || 'Tu contrasena fue restablecida correctamente.';
        this.resetForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;

        if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend este corriendo.';
          return;
        }

        this.errorMessage = error.error?.message || 'No fue posible restablecer la contrasena.';
      }
    });
  }

  private passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('nuevaPassword')?.value;
    const confirmPassword = control.get('confirmarPassword')?.value;

    if (!password || !confirmPassword) {
      return null;
    }

    return password === confirmPassword ? null : { passwordsMismatch: true };
  }
}
