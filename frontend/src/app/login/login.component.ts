import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isLoading = false;
  showError = false;
  showPassword = false;
  errorMessage = 'Credenciales incorrectas. Inténtalo de nuevo.';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si ya esta logueado, redirigir al dashboard
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }

    this.loginForm = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get emailCtrl() { return this.loginForm.get('email')!; }
  get passwordCtrl() { return this.loginForm.get('password')!; }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.showError = false;

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        this.showError = true;
        this.loginForm.get('password')?.setValue('');
        
        if (error.status === 401 || error.status === 403) {
          this.errorMessage = 'Correo o contraseña incorrectos.';
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté corriendo.';
        } else {
          this.errorMessage = error.error?.mensaje || 'Ocurrió un error inesperado. Inténtalo más tarde.';
        }
      }
    });
  }
}
