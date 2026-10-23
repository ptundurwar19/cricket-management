export type MatchStatus = 'UPCOMING' | 'LIVE' | 'INNINGS_BREAK' | 'COMPLETED' | 'PAUSED' | 'ABANDONED';
export type MatchFormat = 'T20' | 'ODI' | 'TEST' | 'BLITZ';
export type WicketType = 'BOWLED' | 'CAUGHT' | 'LBW' | 'RUN_OUT' | 'STUMPED' | 'HIT_WICKET';
export type ExtraType = 'NONE' | 'WIDE' | 'NO_BALL' | 'BYE' | 'LEG_BYE' | 'PENALTY';
export type PlayerRole = 'BATSMAN' | 'BOWLER' | 'ALL_ROUNDER' | 'WICKET_KEEPER';

export interface Player {
  id: number;
  name: string;
  country: string;
  role: PlayerRole;
  battingSkill: number;
  bowlingSkill: number;
  totalMatches: number;
  totalRuns: number;
  totalWickets: number;
  highestScore: number;
  bestBowling: string;
}

export interface Team {
  id: number;
  name: string;
  shortName: string;
  flagEmoji: string;
  primaryColor?: string;
  secondaryColor?: string;
  players: Player[];
}

export interface BatsmanScore {
  id: number;
  playerId: number;
  playerName: string;
  battingOrder: number;
  runs: number;
  balls: number;
  fours: number;
  sixes: number;
  strikeRate: number;
  out: boolean;
  dismissalInfo: string;
  onStrike: boolean;
  batting: boolean;
}

export interface BowlerScore {
  id: number;
  playerId: number;
  playerName: string;
  ballsBowled: number;
  overs: number;
  maidens: number;
  runsConceded: number;
  wickets: number;
  wides: number;
  noBalls: number;
  economy: number;
  current: boolean;
}

export interface FallOfWicket {
  id: number;
  wicketNumber: number;
  scoreAtFall: number;
  overAtFall: string;
  batsmanName: string;
}

export interface Innings {
  id: number;
  inningsNumber: number;
  battingTeamId: number;
  battingTeamName: string;
  bowlingTeamId: number;
  bowlingTeamName: string;
  totalRuns: number;
  totalWickets: number;
  totalOvers: number;
  oversBowled: number;
  legalBallsCount: number;
  wides: number;
  noBalls: number;
  byes: number;
  legByes: number;
  totalExtras: number;
  completed: boolean;
  batsmen: BatsmanScore[];
  bowlers: BowlerScore[];
  fallOfWickets: FallOfWicket[];
}

export interface BallEvent {
  id: number;
  matchId: number;
  inningsNumber: number;
  overNumber: number;
  ballNumberInOver: number;
  ballDisplay: string;
  batsmanName: string;
  bowlerName: string;
  runsScored: number;
  extraType: ExtraType;
  extraRuns: number;
  totalBallRuns: number;
  legalBall: boolean;
  wicket: boolean;
  wicketType?: WicketType;
  outBatsmanName?: string;
  commentary: string;
  timestamp: number;
}

export interface CricketMatch {
  id: number;
  title: string;
  seriesName: string;
  venue: string;
  format: MatchFormat;
  totalOvers: number;
  teamA: Team;
  teamB: Team;
  tossWinnerId: number;
  tossChoice: string;
  status: MatchStatus;
  currentInningsNumber: number;
  firstInnings?: Innings;
  secondInnings?: Innings;
  target?: number;
  requiredRunRate: number;
  currentRunRate: number;
  resultNote: string;
  winnerTeamId?: number;
  winnerTeamName?: string;
  manOfTheMatch?: string;
  simulationSpeedMs: number;
  simulating: boolean;
  recentBalls: string[];
}

export interface LiveScoreUpdateDTO {
  matchId: number;
  title: string;
  status: MatchStatus;
  currentInningsNumber: number;
  battingTeamName: string;
  bowlingTeamName: string;
  totalRuns: number;
  totalWickets: number;
  oversBowled: number;
  totalOvers: number;
  currentRunRate: number;
  requiredRunRate: number;
  target?: number;
  strikerName?: string;
  strikerRuns?: number;
  strikerBalls?: number;
  nonStrikerName?: string;
  nonStrikerRuns?: number;
  nonStrikerBalls?: number;
  currentBowlerName?: string;
  currentBowlerOvers?: number;
  currentBowlerRuns?: number;
  currentBowlerWickets?: number;
  latestCommentary?: string;
  recentBalls?: string[];
  resultNote?: string;
  winnerTeamName?: string;
}

export interface CreateMatchRequest {
  title?: string;
  seriesName?: string;
  venue?: string;
  format: MatchFormat;
  totalOvers: number;
  teamAId: number;
  teamBId: number;
  tossWinnerId?: number;
  tossChoice: string;
}

export interface ManualBallRequest {
  runsScored: number;
  extraType: ExtraType;
  extraRuns: number;
  wicket: boolean;
  wicketType?: WicketType;
  customCommentary?: string;
}

export interface LeaderboardData {
  topRunScorers: Player[];
  topWicketTakers: Player[];
}
