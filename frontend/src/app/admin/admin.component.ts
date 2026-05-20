import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AuthService } from '../core/auth.service';
import { AdminService, FotoPendienteResponse } from '../core/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  userName: string = '';
  estudiantes: FotoPendienteResponse[] = [];
  isLoading = true;
  estadoFiltro: string = 'Todos'; // Estado seleccionado en el filtro
  terminoBusqueda: string = ''; // Término de búsqueda

  // Detail Modal State
  selectedStudent: FotoPendienteResponse | null = null;
  isDetailModalOpen = false;

  // Rejection Modal State
  isRejectModalOpen = false;
  rechazoForm!: FormGroup;
  isSubmittingRechazo = false;

  constructor(
    private authService: AuthService,
    private adminService: AdminService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.userName = user?.name || 'Administrador';

    this.rechazoForm = this.fb.group({
      observacion: ['', [Validators.required, Validators.maxLength(500)]]
    });

    this.cargarEstudiantes();
  }

  cargarEstudiantes(): void {
    this.isLoading = true;
    this.adminService.listarFotosPendientes().subscribe({
      next: (data) => {
        this.estudiantes = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error cargando estudiantes', err);
        this.isLoading = false;
      }
    });
  }

  get estudiantesFiltrados(): FotoPendienteResponse[] {
    let filtrados = this.estudiantes;

    // 1. Filtrar por estado
    if (this.estadoFiltro !== 'Todos') {
      filtrados = filtrados.filter(est => {
        const estado = est.estado?.toLowerCase() || 'pendiente';
        if (this.estadoFiltro === 'Aprobadas' && estado.includes('aprobada')) return true;
        if (this.estadoFiltro === 'Rechazadas' && estado.includes('rechazada')) return true;
        if (this.estadoFiltro === 'Pendientes' && estado.includes('pendiente')) return true;
        return false;
      });
    }

    // 2. Filtrar por texto
    if (this.terminoBusqueda.trim() !== '') {
      const termino = this.terminoBusqueda.toLowerCase().trim();
      filtrados = filtrados.filter(est => {
        const nombreCompleto = `${est.primerNombre || ''} ${est.segundoNombre || ''} ${est.primerApellido || ''} ${est.segundoApellido || ''}`.toLowerCase();
        const documento = est.numeroDocumento?.toString() || '';
        return nombreCompleto.includes(termino) || documento.includes(termino);
      });
    }

    return filtrados;
  }

  abrirDetalle(student: FotoPendienteResponse): void {
    // Si necesitas más detalle, podrías llamar a obtenerDetalleFotoPendiente(student.datosPersonaId)
    // Pero como la lista ya trae todos los campos (según el DTO), podemos usar el objeto directamente.
    this.selectedStudent = student;
    this.isDetailModalOpen = true;
  }

  cerrarDetalle(): void {
    this.isDetailModalOpen = false;
    this.selectedStudent = null;
  }

  aprobarFoto(datosPersonaId: number): void {
    this.adminService.aprobarFoto(datosPersonaId).subscribe({
      next: () => {
        this.cerrarDetalle();
        this.cargarEstudiantes();
      },
      error: (err) => console.error('Error aprobando foto', err)
    });
  }

  abrirRechazoModal(): void {
    this.isRejectModalOpen = true;
    this.rechazoForm.reset();
  }

  cerrarRechazoModal(): void {
    this.isRejectModalOpen = false;
  }

  confirmarRechazo(): void {
    if (this.rechazoForm.invalid || !this.selectedStudent) {
      this.rechazoForm.markAllAsTouched();
      return;
    }

    this.isSubmittingRechazo = true;
    const observacion = this.rechazoForm.value.observacion;
    
    this.adminService.rechazarFoto(this.selectedStudent.datosPersonaId, observacion).subscribe({
      next: () => {
        this.isSubmittingRechazo = false;
        this.cerrarRechazoModal();
        this.cerrarDetalle();
        this.cargarEstudiantes();
      },
      error: (err) => {
        console.error('Error rechazando foto', err);
        this.isSubmittingRechazo = false;
      }
    });
  }

  getPhotoUrl(path: string): string {
    if (!path) return '';
    return path.startsWith('/') ? 'http://localhost:8080' + path : 'http://localhost:8080/' + path;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
