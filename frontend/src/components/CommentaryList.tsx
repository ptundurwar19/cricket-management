import React, { useState } from 'react';
import { BallEvent } from '../types/cricket';
import { MessageSquare, Flame, ShieldAlert, Sparkles } from 'lucide-react';

interface CommentaryListProps {
  commentaries: BallEvent[];
}

export const CommentaryList: React.FC<CommentaryListProps> = ({ commentaries }) => {
  const [filter, setFilter] = useState<'all' | 'boundaries' | 'wickets'>('all');

  const filtered = commentaries.filter((c) => {
    if (filter === 'boundaries') return c.runsScored === 4 || c.runsScored === 6;
    if (filter === 'wickets') return c.wicket;
    return true;
  });

  const getBallBadge = (c: BallEvent) => {
    if (c.wicket) {
      return 'bg-rose-500/20 text-rose-400 border-rose-500/40 font-bold';
    }
    if (c.runsScored === 4) {
      return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40 font-bold';
    }
    if (c.runsScored === 6) {
      return 'bg-purple-500/20 text-purple-300 border-purple-500/40 font-bold';
    }
    if (c.extraType === 'WIDE' || c.extraType === 'NO_BALL') {
      return 'bg-amber-500/20 text-amber-300 border-amber-500/40 font-bold';
    }
    if (c.runsScored === 0) {
      return 'bg-slate-800 text-slate-400 border-slate-700';
    }
    return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/40';
  };

  return (
    <div className="rounded-2xl bg-[#0E1528] border border-slate-800/80 p-5 shadow-xl">
      {/* Header and Filters */}
      <div className="flex flex-wrap items-center justify-between gap-3 border-b border-slate-800 pb-3 mb-4">
        <div className="flex items-center gap-2">
          <MessageSquare className="w-4 h-4 text-emerald-400" />
          <h3 className="text-sm font-bold uppercase tracking-wider text-slate-200">
            Live Ball-by-Ball Commentary
          </h3>
          <span className="text-xs font-mono text-slate-500">
            ({commentaries.length} balls)
          </span>
        </div>

        {/* Filters */}
        <div className="flex items-center gap-1.5 bg-slate-900/60 p-1 rounded-xl border border-slate-800">
          <button
            onClick={() => setFilter('all')}
            className={`px-3 py-1 rounded-lg text-xs font-medium transition-all ${
              filter === 'all'
                ? 'bg-slate-800 text-emerald-400 font-bold shadow-sm'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            All
          </button>
          <button
            onClick={() => setFilter('boundaries')}
            className={`px-3 py-1 rounded-lg text-xs font-medium transition-all flex items-center gap-1 ${
              filter === 'boundaries'
                ? 'bg-emerald-500/20 text-emerald-300 font-bold border border-emerald-500/30'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Sparkles className="w-3 h-3 text-emerald-400" />
            <span>Boundaries</span>
          </button>
          <button
            onClick={() => setFilter('wickets')}
            className={`px-3 py-1 rounded-lg text-xs font-medium transition-all flex items-center gap-1 ${
              filter === 'wickets'
                ? 'bg-rose-500/20 text-rose-300 font-bold border border-rose-500/30'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <ShieldAlert className="w-3 h-3 text-rose-400" />
            <span>Wickets</span>
          </button>
        </div>
      </div>

      {/* Commentary Stream */}
      <div className="space-y-3 max-h-[500px] overflow-y-auto pr-1">
        {filtered.length === 0 ? (
          <div className="text-center py-8 text-slate-500 text-xs italic">
            No deliveries match current filter
          </div>
        ) : (
          filtered.map((c) => (
            <div
              key={c.id || `${c.ballDisplay}-${c.timestamp}`}
              className={`p-3.5 rounded-xl border transition-all ${
                c.wicket
                  ? 'bg-rose-950/20 border-rose-500/30 shadow-md shadow-rose-950/20'
                  : c.runsScored === 4 || c.runsScored === 6
                  ? 'bg-emerald-950/20 border-emerald-500/30 shadow-md shadow-emerald-950/20'
                  : 'bg-slate-900/40 border-slate-800/60 hover:border-slate-700/80'
              }`}
            >
              <div className="flex items-start gap-3">
                {/* Over & Result Badge */}
                <div className="flex flex-col items-center gap-1 flex-shrink-0">
                  <span className="text-xs font-mono font-bold text-slate-400">
                    {c.ballDisplay}
                  </span>
                  <span
                    className={`w-7 h-7 rounded-full border flex items-center justify-center text-xs font-mono ${getBallBadge(
                      c
                    )}`}
                  >
                    {c.wicket
                      ? 'W'
                      : c.extraType === 'WIDE'
                      ? 'Wd'
                      : c.extraType === 'NO_BALL'
                      ? 'Nb'
                      : c.runsScored}
                  </span>
                </div>

                {/* Content */}
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-xs font-bold text-slate-300">
                      {c.bowlerName} to {c.batsmanName}
                    </span>
                    {c.wicket && (
                      <span className="text-[10px] font-bold px-2 py-0.2 rounded-full bg-rose-500 text-white uppercase tracking-wider">
                        WICKET
                      </span>
                    )}
                    {c.runsScored === 4 && (
                      <span className="text-[10px] font-bold px-2 py-0.2 rounded-full bg-emerald-500 text-slate-950 uppercase tracking-wider">
                        FOUR
                      </span>
                    )}
                    {c.runsScored === 6 && (
                      <span className="text-[10px] font-bold px-2 py-0.2 rounded-full bg-purple-500 text-white uppercase tracking-wider">
                        MAXIMUM
                      </span>
                    )}
                  </div>

                  <p className="text-xs text-slate-300 leading-relaxed font-sans">
                    {c.commentary}
                  </p>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
