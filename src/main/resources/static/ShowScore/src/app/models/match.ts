export interface Match {
  id: number;
  team1: string;
  team2: string;
  team1Score: number;
  team2Score: number;
  matchStatus: 'WAITING' | 'IN_PROGRESS' | 'FINISHED' | 'CANCELED';
  date: string;
  startTime: string;
  endTime?: string;
  sportType: string;
}
