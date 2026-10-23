import React from 'react';
import { Activity, Plus, Trophy, Radio, Shield, List } from 'lucide-react';
import { CricketMatch } from '../types/cricket';

interface NavbarProps {
  matches: CricketMatch[];
  selectedMatchId: number | null;
  onSelectMatch: (id: number) => void;
  connectionStatus: 'connected' | 'disconnected' | 'connecting';
  onOpenCreateModal: () => void;
  activeView: 'match' | 'leaderboard';
  setActiveView: (view: 'match' | 'leaderboard') => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  matches,
  selectedMatchId,
  onSelectMatch,
  connectionStatus,
  onOpenCreateModal,
  activeView,
  setActiveView
}) => {
  return (
    <header className="sticky top-0 z-40 bg-[#0B132B]/90 backdrop-blur-md border-b border-slate-800/80 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between gap-4">
        {/* Brand */}
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-400 flex items-center justify-center shadow-lg shadow-emerald-500/20">
            <Radio className="w-5 h-5 text-slate-950 animate-pulse" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-extrabold text-xl tracking-tight text-white">
                Cric<span className="text-emerald-400">Pulse</span>
              </span>
              <span className="text-[10px] uppercase font-bold tracking-wider px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                PRO LIVE
              </span>
            </div>
            <p className="text-[11px] text-slate-400 hidden sm:block">Scalable Real-Time Cricket Engine</p>
          </div>
        </div>

        {/* Match Selector Strip */}
        <div className="hidden md:flex items-center gap-2 overflow-x-auto max-w-xl py-1">
          {matches.map((m) => {
            const isSelected = m.id === selectedMatchId;
            const isLive = m.status === 'LIVE';
            return (
              <button
                key={m.id}
                onClick={() => {
                  onSelectMatch(m.id);
                  setActiveView('match');
                }}
                className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-2 transition-all whitespace-nowrap ${
                  isSelected
                    ? 'bg-slate-800 text-emerald-400 border border-emerald-500/30 shadow-md'
                    : 'bg-slate-900/60 text-slate-400 hover:text-slate-200 hover:bg-slate-800/60 border border-slate-800'
                }`}
              >
                {isLive && (
                  <span className="w-2 h-2 rounded-full bg-red-500 animate-ping inline-block" />
                )}
                <span>
                  {m.teamA.shortName} vs {m.teamB.shortName}
                </span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded font-mono ${
                  isLive ? 'bg-red-500/20 text-red-400' : m.status === 'COMPLETED' ? 'bg-blue-500/20 text-blue-400' : 'bg-slate-700 text-slate-300'
                }`}>
                  {m.status}
                </span>
              </button>
            );
          })}
        </div>

        {/* Right Actions */}
        <div className="flex items-center gap-3">
          {/* Connection Status Pill */}
          <div
            className={`hidden sm:flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-mono font-medium border ${
              connectionStatus === 'connected'
                ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                : connectionStatus === 'connecting'
                ? 'bg-amber-500/10 text-amber-400 border-amber-500/30 animate-pulse'
                : 'bg-red-500/10 text-red-400 border-red-500/30'
            }`}
          >
            <span
              className={`w-2 h-2 rounded-full ${
                connectionStatus === 'connected'
                  ? 'bg-emerald-400 animate-pulse'
                  : connectionStatus === 'connecting'
                  ? 'bg-amber-400'
                  : 'bg-red-400'
              }`}
            />
            <span className="capitalize">{connectionStatus}</span>
          </div>

          {/* Leaderboard view toggle */}
          <button
            onClick={() => setActiveView(activeView === 'match' ? 'leaderboard' : 'match')}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
              activeView === 'leaderboard'
                ? 'bg-amber-500 text-slate-950 shadow-md font-bold'
                : 'bg-slate-800 text-slate-300 hover:text-white hover:bg-slate-700'
            }`}
          >
            <Trophy className="w-4 h-4" />
            <span className="hidden sm:inline">Leaderboard</span>
          </button>

          {/* Create Match Button */}
          <button
            onClick={onOpenCreateModal}
            className="px-3.5 py-1.5 rounded-lg text-xs font-bold bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 flex items-center gap-1.5 shadow-md shadow-emerald-500/20 active:scale-95 transition-all"
          >
            <Plus className="w-4 h-4" />
            <span>New Match</span>
          </button>
        </div>
      </div>
    </header>
  );
};
