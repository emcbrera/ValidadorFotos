import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../core/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-estudiante',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './estudiante.component.html',
  styleUrl: './estudiante.component.css'
})
export class EstudianteComponent implements OnInit {
  registrationForm!: FormGroup;
  userName: string = '';
  isSubmitting = false;
  showSuccess = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.userName = user?.name || 'Estudiante';

    this.registrationForm = this.fb.group({
      primerNombre: ['', Validators.required],
      segundoNombre: [''],
      primerApellido: ['', Validators.required],
      segundoApellido: [''],
      tipoDoc: ['', Validators.required],
      numeroDoc: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      correo: [user?.email || '', [Validators.required, Validators.email]],
      genero: ['', Validators.required],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      foto: [null]
    });
  }

  onFileChange(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      // Para datos quemados, solo guardamos el nombre o un placeholder
      this.registrationForm.patchValue({
        foto: file.name
      });
    }
  }

  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    
    // Simulamos guardado de datos quemados
    console.log('Datos Personales Capturados (MOCK):', this.registrationForm.value);
    
    setTimeout(() => {
      this.isSubmitting = false;
      this.showSuccess = true;
      // No reseteamos para que el usuario vea sus datos "guardados" temporalmente
    }, 1500);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
