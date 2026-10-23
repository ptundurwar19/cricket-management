import React, { useEffect, useState, useCallback } from 'react';
import {
  CricketMatch,
  Team,
  BallEvent,
  CreateMatchRequest,
  ManualBallRequest,
  LiveScoreUpdateDTO
} from './types/cricket';
import {
  getMatches,
  getMatchById,
  getTeams,
  createMatch,
  startSimulation,
  pauseSimulation,
  resumeSimulation,
  resetMatch,
  sendManualBall,
  getCommentary
} from './services/api';
import { realtimeClient } from './services/websocket';
import { Navbar } from './components/Navbar';
import { MatchHeroRibbon } from './components/MatchHeroRibbon';
import { LiveActivePlayers } from './components/LiveActivePlayers';
import { SimulationControls } from './components/SimulationControls';
import { ScorecardView } from './components/ScorecardView';
import { CommentaryList } from './components/CommentaryList';
import { TournamentLeaderboard } from './components/TournamentLeaderboard';
import { CreateMatchModal } from './components/CreateMatchModal';
import { Radio } from 'lucide-react';

export const App: React.FC = () => {
  const [matches, setMatches] = useState<CricketMatch[]>([]);
  const [selectedMatch, setSelectedMatch] = useState<CricketMatch | null>(null);
  const [teams, setTeams] = useState<Team[]>([]);
  const [commentaries, setCommentaries] = useState<BallEvent[]>([]);
  const [connectionStatus, setConnectionStatus] = useState<'connected' | 'disconnected' | 'connecting'>('disconnected');
  const [activeView, setActiveView] = useState<'match' | 'leaderboard'>('match');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);

  // 1. Initial Load of Matches & Teams
  const loadMatches = useCallback(async (selectId?: number) => {
    try {
      const data = await getMatches();
      setMatches(data);

      if (data.length > 0) {
        const targetId = selectId || (selectedMatch ? selectedMatch.id : data[0].id);
        const matchData = await getMatchById(targetId);
        setSelectedMatch(matchData);
        realtimeClient.setFocusedMatch(targetId);

        // Fetch initial commentary
        const comms = await getCommentary(targetId);
        setCommentaries(comms);
      }
    } catch (e) {
      console.error('Failed to load matches:', e);
    } finally {
      setLoading(false);
    }
  }, [selectedMatch]);

  useEffect(() => {
    getTeams().then(setTeams).catch(console.error);
    loadMatches();
  }, []);

  // 2. Realtime WebSocket / SSE connection
  useEffect(() => {
    realtimeClient.connect(selectedMatch?.id);

    const unsubStatus = realtimeClient.onStatusChange(setConnectionStatus);

    const unsubScore = realtimeClient.subscribe((update: LiveScoreUpdateDTO) => {
      // Check if update applies to current match
      if (selectedMatch && update.matchId === selectedMatch.id) {
        setSelectedMatch((prev) => {
          if (!prev) return null;
          const updated = { ...prev };
          updated.status = update.status;
          updated.currentInningsNumber = update.currentInningsNumber;
          updated.currentRunRate = update.currentRunRate;
          updated.requiredRunRate = update.requiredRunRate;
          if (update.target != null) updated.target = update.target;
          if (update.resultNote) updated.resultNote = update.resultNote;
          if (update.winnerTeamName) updated.winnerTeamName = update.winnerTeamName;
          if (update.recentBalls) updated.recentBalls = update.recentBalls;

          const currInn = updated.currentInningsNumber === 1 ? updated.firstInnings : updated.secondInnings;
          if (currInn) {
            currInn.totalRuns = update.totalRuns;
            currInn.totalWickets = update.totalWickets;
            currInn.oversBowled = update.oversBowled;

            // Update active batsman
            if (update.strikerName && currInn.batsmen) {
              const str = currInn.batsmen.find((b) => b.playerName === update.strikerName);
              if (str) {
                str.runs = update.strikerRuns ?? str.runs;
                str.balls = update.strikerBalls ?? str.balls;
                str.onStrike = true;
                str.batting = true;
              }
            }

            if (update.nonStrikerName && currInn.batsmen) {
              const nonStr = currInn.batsmen.find((b) => b.playerName === update.nonStrikerName);
              if (nonStr) {
                nonStr.runs = update.nonStrikerRuns ?? nonStr.runs;
                nonStr.balls = update.nonStrikerBalls ?? nonStr.balls;
                nonStr.onStrike = false;
                nonStr.batting = true;
              }
            }

            // Update active bowler
            if (update.currentBowlerName && currInn.bowlers) {
              const bw = currInn.bowlers.find((b) => b.playerName === update.currentBowlerName);
              if (bw) {
                bw.overs = update.currentBowlerOvers ?? bw.overs;
                bw.runsConceded = update.currentBowlerRuns ?? bw.runsConceded;
                bw.wickets = update.currentBowlerWickets ?? bw.wickets;
                bw.current = true;
              }
            }
          }

          return updated;
        });

        // Also refresh commentary & matches list
        getCommentary(selectedMatch.id).then(setCommentaries).catch(console.error);
        getMatches().then(setMatches).catch(console.error);
      }
    });

    return () => {
      unsubStatus();
      unsubScore();
      realtimeClient.disconnect();
    };
  }, [selectedMatch?.id]);

  // 3. Select different match
  const handleSelectMatch = async (id: number) => {
    try {
      const match = await getMatchById(id);
      setSelectedMatch(match);
      realtimeClient.setFocusedMatch(id);
      const comms = await getCommentary(id);
      setCommentaries(comms);
    } catch (e) {
      console.error('Failed to change match:', e);
    }
  };

  // 4. Match Controls Handlers
  const handleStartSimulation = async (speedMs: number) => {
    if (!selectedMatch) return;
    try {
      await startSimulation(selectedMatch.id, speedMs);
      setSelectedMatch((prev) => prev ? { ...prev, simulating: true, status: 'LIVE' } : null);
      loadMatches(selectedMatch.id);
    } catch (e) {
      console.error(e);
    }
  };

  const handlePauseSimulation = async () => {
    if (!selectedMatch) return;
    try {
      await pauseSimulation(selectedMatch.id);
      setSelectedMatch((prev) => prev ? { ...prev, simulating: false, status: 'PAUSED' } : null);
      loadMatches(selectedMatch.id);
    } catch (e) {
      console.error(e);
    }
  };

  const handleResumeSimulation = async () => {
    if (!selectedMatch) return;
    try {
      await resumeSimulation(selectedMatch.id);
      setSelectedMatch((prev) => prev ? { ...prev, simulating: true, status: 'LIVE' } : null);
      loadMatches(selectedMatch.id);
    } catch (e) {
      console.error(e);
    }
  };

  const handleResetMatch = async () => {
    if (!selectedMatch) return;
    try {
      await resetMatch(selectedMatch.id);
      await loadMatches(selectedMatch.id);
      const comms = await getCommentary(selectedMatch.id);
      setCommentaries(comms);
    } catch (e) {
      console.error(e);
    }
  };

  const handleManualBall = async (req: ManualBallRequest) => {
    if (!selectedMatch) return;
    try {
      await sendManualBall(selectedMatch.id, req);
      const updated = await getMatchById(selectedMatch.id);
      setSelectedMatch(updated);
      const comms = await getCommentary(selectedMatch.id);
      setCommentaries(comms);
    } catch (e) {
      console.error(e);
    }
  };

  const handleCreateMatch = async (req: CreateMatchRequest) => {
    try {
      const newMatch = await createMatch(req);
      await loadMatches(newMatch.id);
    } catch (e) {
      console.error(e);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-[#0A0F1D] flex flex-col items-center justify-center gap-4 text-emerald-400">
        <div className="w-12 h-12 rounded-2xl bg-emerald-500/20 border border-emerald-500/30 flex items-center justify-center animate-spin">
          <Radio className="w-6 h-6 text-emerald-400" />
        </div>
        <p className="font-bold text-sm text-slate-300">Initializing CricPulse Engine...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col bg-[#0A0F1D]">
      {/* Top Navigation */}
      <Navbar
        matches={matches}
        selectedMatchId={selectedMatch?.id || null}
        onSelectMatch={handleSelectMatch}
        connectionStatus={connectionStatus}
        onOpenCreateModal={() => setIsCreateModalOpen(true)}
        activeView={activeView}
        setActiveView={setActiveView}
      />

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 space-y-6">
        {activeView === 'leaderboard' ? (
          <TournamentLeaderboard />
        ) : selectedMatch ? (
          <>
            {/* Top Scoreboard Ribbon */}
            <MatchHeroRibbon match={selectedMatch} />

            {/* Live Batting & Bowling Crease Status */}
            <LiveActivePlayers match={selectedMatch} />

            {/* Simulation Controls & Manual Scoring */}
            <SimulationControls
              match={selectedMatch}
              onStart={handleStartSimulation}
              onPause={handlePauseSimulation}
              onResume={handleResumeSimulation}
              onReset={handleResetMatch}
              onManualBall={handleManualBall}
            />

            {/* Scorecard & Commentary Tabs / Grid */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <ScorecardView match={selectedMatch} />
              <CommentaryList commentaries={commentaries} />
            </div>
          </>
        ) : (
          <div className="text-center py-20 text-slate-400">
            No matches found. Create a match to get started.
          </div>
        )}
      </main>

      {/* Create Match Modal */}
      <CreateMatchModal
        teams={teams}
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onCreate={handleCreateMatch}
      />

      {/* Footer */}
      <footer className="border-t border-slate-900 bg-[#070B16] py-5 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-3">
          <div className="flex items-center gap-2">
            <span className="font-bold text-slate-400">CricPulse Enterprise</span>
            <span>• Real-Time Cricket Score Management Platform</span>
          </div>
          <div className="flex items-center gap-4 text-[11px]">
            <span>Spring Boot 3.3.5 Backend</span>
            <span>•</span>
            <span>WebSocket STOMP + SSE</span>
            <span>•</span>
            <span>TypeScript + React Dashboard</span>
          </div>
        </div>
      </footer>
    </div>
  );
};
