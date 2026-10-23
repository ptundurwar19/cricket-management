import {
  CricketMatch,
  MatchStatus,
  CreateMatchRequest,
  ManualBallRequest,
  BallEvent,
  LeaderboardData,
  Team,
  LiveScoreUpdateDTO
} from '../types/cricket';

const BASE_URL = '/api';

export async function getMatches(status?: MatchStatus): Promise<CricketMatch[]> {
  const url = status ? `${BASE_URL}/matches?status=${status}` : `${BASE_URL}/matches`;
  const res = await fetch(url);
  if (!res.ok) throw new Error(`Failed to fetch matches: ${res.statusText}`);
  return res.json();
}

export async function getMatchById(id: number): Promise<CricketMatch> {
  const res = await fetch(`${BASE_URL}/matches/${id}`);
  if (!res.ok) throw new Error(`Failed to fetch match ${id}: ${res.statusText}`);
  return res.json();
}

export async function createMatch(req: CreateMatchRequest): Promise<CricketMatch> {
  const res = await fetch(`${BASE_URL}/matches`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req)
  });
  if (!res.ok) throw new Error(`Failed to create match: ${res.statusText}`);
  return res.json();
}

export async function startSimulation(id: number, speedMs = 1500): Promise<void> {
  const res = await fetch(`${BASE_URL}/matches/${id}/start?speedMs=${speedMs}`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error(`Failed to start simulation: ${res.statusText}`);
}

export async function pauseSimulation(id: number): Promise<void> {
  const res = await fetch(`${BASE_URL}/matches/${id}/pause`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error(`Failed to pause simulation: ${res.statusText}`);
}

export async function resumeSimulation(id: number): Promise<void> {
  const res = await fetch(`${BASE_URL}/matches/${id}/resume`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error(`Failed to resume simulation: ${res.statusText}`);
}

export async function resetMatch(id: number): Promise<void> {
  const res = await fetch(`${BASE_URL}/matches/${id}/reset`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error(`Failed to reset match: ${res.statusText}`);
}

export async function sendManualBall(id: number, req: ManualBallRequest): Promise<LiveScoreUpdateDTO> {
  const res = await fetch(`${BASE_URL}/matches/${id}/ball`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req)
  });
  if (!res.ok) throw new Error(`Failed to record ball: ${res.statusText}`);
  return res.json();
}

export async function getCommentary(matchId: number): Promise<BallEvent[]> {
  const res = await fetch(`${BASE_URL}/matches/${matchId}/commentary`);
  if (!res.ok) throw new Error(`Failed to fetch commentary: ${res.statusText}`);
  return res.json();
}

export async function getLeaderboard(): Promise<LeaderboardData> {
  const res = await fetch(`${BASE_URL}/matches/leaderboard`);
  if (!res.ok) throw new Error(`Failed to fetch leaderboard: ${res.statusText}`);
  return res.json();
}

export async function getTeams(): Promise<Team[]> {
  const res = await fetch(`${BASE_URL}/teams`);
  if (!res.ok) throw new Error(`Failed to fetch teams: ${res.statusText}`);
  return res.json();
}
