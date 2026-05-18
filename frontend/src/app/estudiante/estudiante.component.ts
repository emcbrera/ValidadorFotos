import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../core/auth.service';
import { EstudianteService, DatosPersonaResponse, DatosPersonaRequest } from '../core/estudiante.service';
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
  successMessage = '';
  showError = false;
  errorMessage = '';

  isEditMode = false;
  estadoActual: string | null = null;
  observacionRechazo: string | null = null;
  currentPhotoUrl: string | null = null;
  selectedFile: File | null = null;
  selectedFileName: string | null = null;

  isImageModalOpen = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private estudianteService: EstudianteService,
    private router: Router
  ) {}

  openImageModal(): void {
    if (this.currentPhotoUrl) {
      this.isImageModalOpen = true;
    }
  }

  closeImageModal(): void {
    this.isImageModalOpen = false;
  }

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.userName = user?.name || 'Estudiante';

    this.registrationForm = this.fb.group({
      primerNombre: ['', Validators.required],
      segundoNombre: [''],
      primerApellido: ['', Validators.required],
      segundoApellido: [''],
      tipoDocumento: ['', Validators.required],
      numeroDocumento: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      correo: [{ value: user?.email || '', disabled: true }, [Validators.required, Validators.email]],
      genero: ['', Validators.required],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]+$')]]
    });

    this.cargarDatosActuales();
  }

  cargarDatosActuales(): void {
    this.estudianteService.obtenerDatosPersonales().subscribe({
      next: (datos: DatosPersonaResponse) => {
        if (datos) {
          this.isEditMode = true;
          this.estadoActual = datos.estado || 'Pendiente';
          this.observacionRechazo = datos.observacionRevision || null;

          this.registrationForm.patchValue({
            primerNombre: datos.primerNombre,
            segundoNombre: datos.segundoNombre,
            primerApellido: datos.primerApellido,
            segundoApellido: datos.segundoApellido,
            tipoDocumento: datos.tipoDocumento,
            numeroDocumento: datos.numeroDocumento,
            correo: datos.correo,
            genero: datos.genero,
            celular: datos.celular
          });
          if (datos.fotoUrl) {
            this.currentPhotoUrl = datos.fotoUrl.startsWith('/') 
              ? 'http://localhost:8080' + datos.fotoUrl 
              : 'http://localhost:8080/' + datos.fotoUrl;
          }
        }
      },
      error: (err) => {
        console.log('No se encontraron datos previos o hubo un error, modo creación activo.', err);
        this.isEditMode = false;
      }
    });
  }

  onFileChange(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
      this.selectedFileName = this.selectedFile ? this.selectedFile.name : null;
    } else {
      this.selectedFile = null;
      this.selectedFileName = null;
    }
  }

  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    if (!this.isEditMode && !this.selectedFile) {
      this.showError = true;
      this.errorMessage = 'Debes subir una fotografía de perfil para registrarte.';
      setTimeout(() => this.showError = false, 3000);
      return;
    }

    this.isSubmitting = true;
    this.showSuccess = false;
    this.showError = false;

    if (this.isEditMode) {
      // MODO EDICIÓN
      const request: DatosPersonaRequest = this.registrationForm.getRawValue();
      this.estudianteService.actualizarDatosPersonales(request).subscribe({
        next: (response) => {
          if (this.selectedFile) {
            this.estudianteService.actualizarFoto(this.selectedFile).subscribe({
              next: (resFoto) => {
                this.finalizarGuardado('Información y foto actualizadas correctamente.', resFoto.fotoUrl);
              },
              error: (err) => this.manejarError('Error al actualizar la foto.', err)
            });
          } else {
            this.finalizarGuardado('Información actualizada correctamente.', response.fotoUrl);
          }
        },
        error: (err) => this.manejarError('Error al actualizar la información.', err)
      });

    } else {
      // MODO CREACIÓN
      if (this.selectedFile) {
        this.estudianteService.registrarDatosPersonales(this.registrationForm.getRawValue(), this.selectedFile).subscribe({
          next: (response) => {
            this.isEditMode = true; // Pasar a modo edición tras crear
            this.finalizarGuardado('Datos registrados exitosamente.', response.fotoUrl);
          },
          error: (err) => this.manejarError('Error al registrar los datos.', err)
        });
      }
    }
  }

  private finalizarGuardado(mensaje: string, fotoUrl?: string): void {
    this.isSubmitting = false;
    this.showSuccess = true;
    this.successMessage = mensaje;
    
    // Al guardar o actualizar, el estado vuelve a Pendiente
    this.estadoActual = 'Pendiente';
    this.observacionRechazo = null;

    if (fotoUrl) {
       this.currentPhotoUrl = fotoUrl.startsWith('/') 
         ? 'http://localhost:8080' + fotoUrl 
         : 'http://localhost:8080/' + fotoUrl;
    }
    setTimeout(() => this.showSuccess = false, 5000);
  }

  private manejarError(mensaje: string, err: any): void {
    this.isSubmitting = false;
    this.showError = true;
    this.errorMessage = err.error?.mensaje || mensaje;
    setTimeout(() => this.showError = false, 5000);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
