import React from 'react';

interface RecentBallsTrackerProps {
  recentBalls: string[];
}

export const RecentBallsTracker: React.FC<RecentBallsTrackerProps> = ({ recentBalls }) => {
  const getBadgeStyle = (tag: string) => {
    if (tag === 'W' || tag.startsWith('W+')) {
      return 'bg-rose-500/20 text-rose-400 border-rose-500/50 shadow-rose-500/20 shadow-md font-extrabold animate-pulse';
    }
    if (tag === '4') {
      return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/50 shadow-emerald-500/20 shadow-sm font-extrabold';
    }
    if (tag === '6') {
      return 'bg-purple-500/20 text-purple-300 border-purple-500/50 shadow-purple-500/20 shadow-md font-extrabold ring-1 ring-purple-400/30';
    }
    if (tag === '0') {
      return 'bg-slate-800/80 text-slate-400 border-slate-700/60 font-medium';
    }
    if (tag.startsWith('Wd') || tag.startsWith('Nb')) {
      return 'bg-amber-500/20 text-amber-300 border-amber-500/50 font-bold';
    }
    // 1, 2, 3
    return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/40 font-semibold';
  };

  return (
    <div className="flex items-center gap-2 overflow-x-auto py-2 px-1">
      <span className="text-xs uppercase tracking-wider text-slate-400 font-bold whitespace-nowrap mr-1">
        Recent:
      </span>
      {recentBalls.length === 0 ? (
        <span className="text-xs text-slate-500 italic">No balls bowled in this session yet</span>
      ) : (
        recentBalls.map((tag, idx) => (
          <span
            key={idx}
            className={`w-7 h-7 flex-shrink-0 rounded-full border flex items-center justify-center text-xs font-mono transition-transform hover:scale-110 cursor-default ${getBadgeStyle(
              tag
            )}`}
            title={`Ball ${idx + 1}: ${tag}`}
          >
            {tag}
          </span>
        ))
      )}
    </div>
  );
};
