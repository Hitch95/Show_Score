export interface User {
  id?: number;
  username: string;
  email: string;
  password?: string;
  role: 'ADMIN' | 'ORGANIZER' | 'USER';
  isWatchingMatch: boolean;
}
