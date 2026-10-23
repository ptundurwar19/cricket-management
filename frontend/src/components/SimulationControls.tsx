import React, { useState } from 'react';
import { Play, Pause, RotateCcw, FastForward, StepForward, Send, ShieldAlert, CheckCircle2 } from 'lucide-react';
import { CricketMatch, ExtraType, WicketType, ManualBallRequest } from '../types/cricket';

interface SimulationControlsProps {
  match: CricketMatch;
  onStart: (speedMs: number) => void;
  onPause: () => void;
  onResume: () => void;
  onReset: () => void;
  onManualBall: (req: ManualBallRequest) => void;
}

export const SimulationControls: React.FC<SimulationControlsProps> = ({
  match,
  onStart,
  onPause,
  onResume,
  onReset,
  onManualBall
}) => {
  const [speed, setSpeed] = useState<number>(1500);
  const [activeTab, setActiveTab] = useState<'simulator' | 'manual'>('simulator');

  // Manual scoring state
  const [manualRuns, setManualRuns] = useState<number>(0);
  const [manualExtra, setManualExtra] = useState<ExtraType>('NONE');
  const [isWicket, setIsWicket] = useState<boolean>(false);
  const [wicketType, setWicketType] = useState<WicketType>('CAUGHT');
  const [customCommentary, setCustomCommentary] = useState<string>('');

  const handleManualSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const extraRuns = (manualExtra === 'WIDE' || manualExtra === 'NO_BALL') ? 1 : 0;
    onManualBall({
      runsScored: manualRuns,
      extraType: manualExtra,
      extraRuns: extraRuns,
      wicket: isWicket,
      wicketType: isWicket ? wicketType : undefined,
      customCommentary: customCommentary.trim() ? customCommentary : undefined
    });

    // Reset some fields
    setManualRuns(0);
    setManualExtra('NONE');
    setIsWicket(false);
    setCustomCommentary('');
  };

  const isCompleted = match.status === 'COMPLETED';

  return (
    <div className="rounded-2xl bg-[#0E1528] border border-slate-800/80 p-5 shadow-xl">
      {/* Tab Switcher */}
      <div className="flex items-center justify-between border-b border-slate-800 pb-3 mb-5">
        <div className="flex items-center gap-2">
          <button
            onClick={() => setActiveTab('simulator')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
              activeTab === 'simulator'
                ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            ⚡ Auto Live Simulator
          </button>
          <button
            onClick={() => setActiveTab('manual')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
              activeTab === 'manual'
                ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            📋 Official Umpire / Scorer Pad
          </button>
        </div>

        <span className="text-xs font-mono text-slate-500 hidden sm:inline">
          Match ID: #{match.id}
        </span>
      </div>

      {activeTab === 'simulator' ? (
        <div>
          <div className="flex flex-wrap items-center justify-between gap-4">
            {/* Playback Controls */}
            <div className="flex items-center gap-2">
              {match.simulating ? (
                <button
                  onClick={onPause}
                  className="px-4 py-2 rounded-xl bg-amber-500 hover:bg-amber-400 text-slate-950 font-bold text-xs flex items-center gap-2 shadow-lg shadow-amber-500/20 active:scale-95 transition-all"
                >
                  <Pause className="w-4 h-4 fill-current" />
                  <span>Pause Match</span>
                </button>
              ) : (
                <button
                  onClick={() => onStart(speed)}
                  disabled={isCompleted}
                  className="px-4 py-2 rounded-xl bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 font-bold text-xs flex items-center gap-2 shadow-lg shadow-emerald-500/20 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
                >
                  <Play className="w-4 h-4 fill-current" />
                  <span>{match.status === 'PAUSED' ? 'Resume Match' : 'Start Simulation'}</span>
                </button>
              )}

              {/* Step 1 Ball */}
              <button
                onClick={() => onManualBall({ runsScored: 0, extraType: 'NONE', extraRuns: 0, wicket: false })}
                disabled={isCompleted || match.simulating}
                className="px-3.5 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold text-xs flex items-center gap-1.5 border border-slate-700 disabled:opacity-40 disabled:cursor-not-allowed active:scale-95 transition-all"
                title="Advance 1 ball automatically"
              >
                <StepForward className="w-4 h-4" />
                <span>Next Ball</span>
              </button>

              {/* Reset Match */}
              <button
                onClick={onReset}
                className="px-3.5 py-2 rounded-xl bg-rose-500/10 hover:bg-rose-500/20 text-rose-300 font-semibold text-xs flex items-center gap-1.5 border border-rose-500/20 active:scale-95 transition-all"
                title="Reset match to initial state"
              >
                <RotateCcw className="w-4 h-4" />
                <span>Reset</span>
              </button>
            </div>

            {/* Simulation Speed Selector */}
            <div className="flex items-center gap-2 bg-slate-900/60 p-1.5 rounded-xl border border-slate-800">
              <span className="text-[11px] font-bold text-slate-400 uppercase tracking-wider pl-2 pr-1">
                Speed:
              </span>
              {[
                { label: '1x (Slow)', ms: 3000 },
                { label: '2x (Normal)', ms: 1500 },
                { label: '4x (Fast)', ms: 700 }
              ].map((s) => (
                <button
                  key={s.ms}
                  onClick={() => {
                    setSpeed(s.ms);
                    if (match.simulating) {
                      onStart(s.ms);
                    }
                  }}
                  className={`px-2.5 py-1 rounded-lg text-xs font-mono font-medium transition-all ${
                    speed === s.ms
                      ? 'bg-emerald-500 text-slate-950 font-bold shadow-sm'
                      : 'text-slate-400 hover:text-white'
                  }`}
                >
                  {s.label}
                </button>
              ))}
            </div>
          </div>

          <div className="mt-4 text-xs text-slate-400 flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-emerald-400 inline-block" />
            <span>
              Real-time engine generates realistic deliveries, strike rotations, bowler changes, and commentary based on cricket probability models.
            </span>
          </div>
        </div>
      ) : (
        /* Manual Scoring Console */
        <form onSubmit={handleManualSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* Runs Selection */}
            <div>
              <label className="block text-xs font-bold text-slate-300 uppercase tracking-wider mb-2">
                Runs Off Bat:
              </label>
              <div className="flex items-center gap-1.5 flex-wrap">
                {[0, 1, 2, 3, 4, 6].map((r) => (
                  <button
                    type="button"
                    key={r}
                    onClick={() => setManualRuns(r)}
                    className={`w-9 h-9 rounded-lg font-mono font-bold text-sm transition-all ${
                      manualRuns === r
                        ? 'bg-emerald-500 text-slate-950 scale-105 shadow-md shadow-emerald-500/20'
                        : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
                    }`}
                  >
                    {r}
                  </button>
                ))}
              </div>
            </div>

            {/* Extras Selection */}
            <div>
              <label className="block text-xs font-bold text-slate-300 uppercase tracking-wider mb-2">
                Extras:
              </label>
              <div className="flex items-center gap-1.5 flex-wrap">
                {(['NONE', 'WIDE', 'NO_BALL', 'BYE', 'LEG_BYE'] as ExtraType[]).map((ex) => (
                  <button
                    type="button"
                    key={ex}
                    onClick={() => setManualExtra(ex)}
                    className={`px-2.5 py-1.5 rounded-lg text-xs font-semibold uppercase transition-all ${
                      manualExtra === ex
                        ? 'bg-amber-500 text-slate-950 font-bold shadow-md shadow-amber-500/20'
                        : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
                    }`}
                  >
                    {ex === 'NONE' ? 'Legal' : ex}
                  </button>
                ))}
              </div>
            </div>

            {/* Wicket Toggle */}
            <div>
              <label className="block text-xs font-bold text-slate-300 uppercase tracking-wider mb-2">
                Wicket:
              </label>
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => setIsWicket(!isWicket)}
                  className={`px-3 py-1.5 rounded-lg text-xs font-bold flex items-center gap-1.5 transition-all ${
                    isWicket
                      ? 'bg-rose-500 text-white shadow-md shadow-rose-500/20'
                      : 'bg-slate-800 text-slate-400 hover:bg-slate-700'
                  }`}
                >
                  <ShieldAlert className="w-4 h-4" />
                  <span>{isWicket ? 'WICKET (OUT!)' : 'No Wicket'}</span>
                </button>

                {isWicket && (
                  <select
                    value={wicketType}
                    onChange={(e) => setWicketType(e.target.value as WicketType)}
                    className="bg-slate-800 text-slate-200 text-xs rounded-lg px-2.5 py-1.5 border border-slate-700 focus:outline-none focus:border-rose-400"
                  >
                    <option value="CAUGHT">Caught</option>
                    <option value="BOWLED">Bowled</option>
                    <option value="LBW">LBW</option>
                    <option value="RUN_OUT">Run Out</option>
                    <option value="STUMPED">Stumped</option>
                  </select>
                )}
              </div>
            </div>
          </div>

          {/* Custom commentary input */}
          <div className="flex items-center gap-3">
            <input
              type="text"
              placeholder="Optional broadcast note (e.g. 'Glorious straight drive for four!')"
              value={customCommentary}
              onChange={(e) => setCustomCommentary(e.target.value)}
              className="flex-1 bg-slate-900 border border-slate-800 rounded-xl px-4 py-2 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-emerald-500"
            />
            <button
              type="submit"
              disabled={isCompleted}
              className="px-5 py-2 rounded-xl bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 font-bold text-xs flex items-center gap-1.5 shadow-md shadow-emerald-500/20 active:scale-95 disabled:opacity-50 transition-all"
            >
              <Send className="w-3.5 h-3.5" />
              <span>Record Ball</span>
            </button>
          </div>
        </form>
      )}
    </div>
  );
};
