export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresIn: number;
  username: string;
  correo: string;
  rol: string;
  mensaje: string;
}
