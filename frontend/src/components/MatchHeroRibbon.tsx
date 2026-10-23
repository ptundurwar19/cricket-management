import React, { useEffect } from 'react';
import confetti from 'canvas-confetti';
import { Trophy, Flame, Target, MapPin, Calendar, Clock } from 'lucide-react';
import { CricketMatch } from '../types/cricket';
import { RecentBallsTracker } from './RecentBallsTracker';

interface MatchHeroRibbonProps {
  match: CricketMatch;
}

export const MatchHeroRibbon: React.FC<MatchHeroRibbonProps> = ({ match }) => {
  const currentInnings = match.currentInningsNumber === 1 ? match.firstInnings : match.secondInnings;
  const isSecondInnings = match.currentInningsNumber === 2 && match.target != null;

  // Trigger celebration confetti on match completion
  useEffect(() => {
    if (match.status === 'COMPLETED' && match.winnerTeamName) {
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 }
      });
    }
  }, [match.status, match.winnerTeamName]);

  const ballsRemaining = isSecondInnings && currentInnings
    ? (match.totalOvers * 6) - currentInnings.legalBallsCount
    : 0;
  const runsRemaining = isSecondInnings && currentInnings && match.target
    ? Math.max(0, match.target - currentInnings.totalRuns)
    : 0;

  const chaseProgressPercent = isSecondInnings && currentInnings && match.target
    ? Math.min(100, Math.round((currentInnings.totalRuns / match.target) * 100))
    : 0;

  return (
    <div className="relative overflow-hidden rounded-2xl bg-gradient-to-br from-[#121A33] via-[#0E162B] to-[#0A1020] border border-slate-800 shadow-2xl p-5 md:p-7">
      {/* Background stadium ambient glow */}
      <div className="absolute top-0 right-1/4 w-96 h-96 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-0 left-10 w-80 h-80 bg-blue-500/10 rounded-full blur-3xl pointer-events-none" />

      {/* Top Meta Header */}
      <div className="flex flex-wrap items-center justify-between gap-3 border-b border-slate-800/80 pb-4 mb-5">
        <div className="flex items-center gap-2 text-xs font-medium text-slate-400">
          <span className="px-2.5 py-0.5 rounded-md bg-slate-800 text-slate-200 font-bold tracking-wider">
            {match.format}
          </span>
          <span className="text-slate-600">•</span>
          <span className="text-slate-300 font-semibold">{match.seriesName}</span>
          <span className="text-slate-600">•</span>
          <div className="flex items-center gap-1 text-slate-400">
            <MapPin className="w-3.5 h-3.5 text-slate-500" />
            <span>{match.venue}</span>
          </div>
        </div>

        {/* Status Pill */}
        <div className="flex items-center gap-2">
          {match.status === 'LIVE' && (
            <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-red-500/20 text-red-400 border border-red-500/30 text-xs font-bold uppercase tracking-wider">
              <span className="w-2 h-2 rounded-full bg-red-500 animate-ping inline-block" />
              <span>LIVE • INNINGS {match.currentInningsNumber}</span>
            </div>
          )}
          {match.status === 'INNINGS_BREAK' && (
            <div className="px-3 py-1 rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/30 text-xs font-bold uppercase tracking-wider">
              Innings Break
            </div>
          )}
          {match.status === 'COMPLETED' && (
            <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 text-xs font-bold uppercase tracking-wider">
              <Trophy className="w-3.5 h-3.5" />
              <span>Completed</span>
            </div>
          )}
          {match.status === 'UPCOMING' && (
            <div className="px-3 py-1 rounded-full bg-blue-500/20 text-blue-400 border border-blue-500/30 text-xs font-bold uppercase tracking-wider">
              Upcoming
            </div>
          )}
          {match.status === 'PAUSED' && (
            <div className="px-3 py-1 rounded-full bg-yellow-500/20 text-yellow-400 border border-yellow-500/30 text-xs font-bold uppercase tracking-wider">
              Paused
            </div>
          )}
        </div>
      </div>

      {/* Main Scoreboard Display */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 items-center">
        {/* Team A Display */}
        <div className="flex items-center justify-between p-4 rounded-xl bg-slate-900/40 border border-slate-800/60 backdrop-blur-sm">
          <div className="flex items-center gap-3.5">
            <span className="text-3xl filter drop-shadow">{match.teamA.flagEmoji}</span>
            <div>
              <div className="font-extrabold text-lg text-white tracking-tight flex items-center gap-2">
                {match.teamA.name}
                <span className="text-xs text-slate-400 font-mono font-medium">({match.teamA.shortName})</span>
              </div>
              <div className="text-xs text-slate-400">
                {match.tossWinnerId === match.teamA.id && (
                  <span className="text-emerald-400 font-medium">Toss won ({match.tossChoice})</span>
                )}
              </div>
            </div>
          </div>

          <div className="text-right">
            {match.firstInnings?.battingTeamId === match.teamA.id ? (
              <div>
                <div className="text-2xl font-mono-score font-black text-white">
                  {match.firstInnings.totalRuns}
                  <span className="text-slate-400 text-lg">/{match.firstInnings.totalWickets}</span>
                </div>
                <div className="text-xs font-mono text-slate-400">
                  ({match.firstInnings.oversBowled} / {match.firstInnings.totalOvers} ov)
                </div>
              </div>
            ) : match.secondInnings?.battingTeamId === match.teamA.id ? (
              <div>
                <div className="text-2xl font-mono-score font-black text-white">
                  {match.secondInnings.totalRuns}
                  <span className="text-slate-400 text-lg">/{match.secondInnings.totalWickets}</span>
                </div>
                <div className="text-xs font-mono text-slate-400">
                  ({match.secondInnings.oversBowled} / {match.secondInnings.totalOvers} ov)
                </div>
              </div>
            ) : (
              <span className="text-xs text-slate-500 font-medium">Yet to bat</span>
            )}
          </div>
        </div>

        {/* Team B Display */}
        <div className="flex items-center justify-between p-4 rounded-xl bg-slate-900/40 border border-slate-800/60 backdrop-blur-sm">
          <div className="flex items-center gap-3.5">
            <span className="text-3xl filter drop-shadow">{match.teamB.flagEmoji}</span>
            <div>
              <div className="font-extrabold text-lg text-white tracking-tight flex items-center gap-2">
                {match.teamB.name}
                <span className="text-xs text-slate-400 font-mono font-medium">({match.teamB.shortName})</span>
              </div>
              <div className="text-xs text-slate-400">
                {match.tossWinnerId === match.teamB.id && (
                  <span className="text-emerald-400 font-medium">Toss won ({match.tossChoice})</span>
                )}
              </div>
            </div>
          </div>

          <div className="text-right">
            {match.firstInnings?.battingTeamId === match.teamB.id ? (
              <div>
                <div className="text-2xl font-mono-score font-black text-white">
                  {match.firstInnings.totalRuns}
                  <span className="text-slate-400 text-lg">/{match.firstInnings.totalWickets}</span>
                </div>
                <div className="text-xs font-mono text-slate-400">
                  ({match.firstInnings.oversBowled} / {match.firstInnings.totalOvers} ov)
                </div>
              </div>
            ) : match.secondInnings?.battingTeamId === match.teamB.id ? (
              <div>
                <div className="text-2xl font-mono-score font-black text-white">
                  {match.secondInnings.totalRuns}
                  <span className="text-slate-400 text-lg">/{match.secondInnings.totalWickets}</span>
                </div>
                <div className="text-xs font-mono text-slate-400">
                  ({match.secondInnings.oversBowled} / {match.secondInnings.totalOvers} ov)
                </div>
              </div>
            ) : (
              <span className="text-xs text-slate-500 font-medium">Yet to bat</span>
            )}
          </div>
        </div>
      </div>

      {/* Target Chase Progress Strip (2nd Innings) */}
      {isSecondInnings && (
        <div className="mt-5 p-4 rounded-xl bg-gradient-to-r from-emerald-950/40 to-teal-950/40 border border-emerald-500/20">
          <div className="flex flex-wrap items-center justify-between gap-3 text-xs mb-2">
            <div className="flex items-center gap-2 font-bold text-emerald-400">
              <Target className="w-4 h-4" />
              <span>Target: {match.target} runs</span>
            </div>
            <div className="flex items-center gap-4 text-slate-300 font-mono">
              <span>
                Need <strong className="text-white font-extrabold">{runsRemaining}</strong> runs from{' '}
                <strong className="text-white font-extrabold">{ballsRemaining}</strong> balls
              </span>
              <span>
                RRR: <strong className="text-amber-300">{match.requiredRunRate}</strong>
              </span>
              <span>
                CRR: <strong className="text-cyan-300">{match.currentRunRate}</strong>
              </span>
            </div>
          </div>

          {/* Animated Bar */}
          <div className="w-full h-2.5 bg-slate-800 rounded-full overflow-hidden relative">
            <div
              className="h-full bg-gradient-to-r from-teal-500 via-emerald-400 to-green-300 transition-all duration-500 ease-out rounded-full"
              style={{ width: `${chaseProgressPercent}%` }}
            />
          </div>
        </div>
      )}

      {/* Recent Balls Strip */}
      <div className="mt-4 pt-3 border-t border-slate-800/60 flex items-center justify-between flex-wrap gap-2">
        <RecentBallsTracker recentBalls={match.recentBalls || []} />
        {currentInnings && (
          <div className="text-xs text-slate-400 font-mono flex items-center gap-3">
            <span>
              Extras: <strong className="text-slate-200">{currentInnings.totalExtras}</strong> (w {currentInnings.wides}, nb {currentInnings.noBalls})
            </span>
            <span>
              CRR: <strong className="text-emerald-400">{match.currentRunRate}</strong>
            </span>
          </div>
        )}
      </div>

      {/* Match Outcome Result Banner */}
      {match.resultNote && (
        <div className={`mt-4 p-3 rounded-xl flex items-center justify-between flex-wrap gap-2 text-sm font-semibold border ${
          match.status === 'COMPLETED'
            ? 'bg-gradient-to-r from-emerald-950/70 to-teal-950/70 border-emerald-500/40 text-emerald-200'
            : 'bg-slate-900/60 border-slate-800 text-slate-300'
        }`}>
          <div className="flex items-center gap-2">
            {match.status === 'COMPLETED' ? (
              <Trophy className="w-5 h-5 text-amber-400 flex-shrink-0 animate-bounce" />
            ) : (
              <Flame className="w-4 h-4 text-emerald-400 flex-shrink-0" />
            )}
            <span>{match.resultNote}</span>
          </div>
          {match.manOfTheMatch && (
            <div className="text-xs bg-amber-500/20 text-amber-300 border border-amber-500/30 px-3 py-1 rounded-full font-bold">
              ★ POTM: {match.manOfTheMatch}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
