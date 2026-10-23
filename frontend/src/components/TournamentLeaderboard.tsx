import React, { useEffect, useState } from 'react';
import { Trophy, Award, Flame, ShieldCheck } from 'lucide-react';
import { LeaderboardData } from '../types/cricket';
import { getLeaderboard } from '../services/api';

export const TournamentLeaderboard: React.FC = () => {
  const [data, setData] = useState<LeaderboardData | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    getLeaderboard()
      .then((res) => {
        setData(res);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Failed to load leaderboard:', err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center p-12 text-slate-400">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-emerald-400" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Title */}
      <div className="flex items-center justify-between border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-xl font-black text-white flex items-center gap-2">
            <Trophy className="w-6 h-6 text-amber-400" />
            <span>Tournament Stats & Leaderboards</span>
          </h2>
          <p className="text-xs text-slate-400">
            Official season player statistics and leaderboard caps
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Orange Cap (Most Runs) */}
        <div className="rounded-2xl bg-[#0E1528] border border-amber-500/30 p-5 shadow-xl relative overflow-hidden">
          <div className="absolute top-0 right-0 w-32 h-32 bg-amber-500/10 rounded-full blur-2xl pointer-events-none" />

          <div className="flex items-center gap-3 border-b border-slate-800 pb-3 mb-4">
            <div className="w-10 h-10 rounded-xl bg-amber-500/20 text-amber-400 flex items-center justify-center border border-amber-500/30 shadow-inner">
              🧢
            </div>
            <div>
              <h3 className="text-sm font-extrabold uppercase tracking-wider text-amber-400">
                Orange Cap • Top Run Scorers
              </h3>
              <p className="text-[11px] text-slate-400">Most prolific batsman of the tournament</p>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs font-mono">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 font-sans">
                  <th className="py-2">Rank</th>
                  <th className="py-2">Player</th>
                  <th className="py-2 text-right">Inns</th>
                  <th className="py-2 text-right">Runs</th>
                  <th className="py-2 text-right">HS</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/40">
                {data?.topRunScorers?.map((p, idx) => (
                  <tr
                    key={p.id}
                    className={idx === 0 ? 'bg-amber-500/10 text-amber-300 font-bold' : 'text-slate-300'}
                  >
                    <td className="py-2.5">
                      {idx === 0 ? '🥇 1' : idx === 1 ? '🥈 2' : idx === 2 ? '🥉 3' : `#${idx + 1}`}
                    </td>
                    <td className="py-2.5 font-sans font-semibold">
                      <div>{p.name}</div>
                      <div className="text-[10px] text-slate-500 font-normal">{p.country}</div>
                    </td>
                    <td className="py-2.5 text-right text-slate-400">{p.totalMatches}</td>
                    <td className="py-2.5 text-right font-bold text-white text-sm">{p.totalRuns}</td>
                    <td className="py-2.5 text-right text-slate-300">{p.highestScore}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Purple Cap (Most Wickets) */}
        <div className="rounded-2xl bg-[#0E1528] border border-purple-500/30 p-5 shadow-xl relative overflow-hidden">
          <div className="absolute top-0 right-0 w-32 h-32 bg-purple-500/10 rounded-full blur-2xl pointer-events-none" />

          <div className="flex items-center gap-3 border-b border-slate-800 pb-3 mb-4">
            <div className="w-10 h-10 rounded-xl bg-purple-500/20 text-purple-400 flex items-center justify-center border border-purple-500/30 shadow-inner">
              🧢
            </div>
            <div>
              <h3 className="text-sm font-extrabold uppercase tracking-wider text-purple-400">
                Purple Cap • Top Wicket Takers
              </h3>
              <p className="text-[11px] text-slate-400">Deadliest bowlers of the tournament</p>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs font-mono">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 font-sans">
                  <th className="py-2">Rank</th>
                  <th className="py-2">Player</th>
                  <th className="py-2 text-right">Matches</th>
                  <th className="py-2 text-right">Wickets</th>
                  <th className="py-2 text-right">BBI</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/40">
                {data?.topWicketTakers?.map((p, idx) => (
                  <tr
                    key={p.id}
                    className={idx === 0 ? 'bg-purple-500/10 text-purple-300 font-bold' : 'text-slate-300'}
                  >
                    <td className="py-2.5">
                      {idx === 0 ? '🥇 1' : idx === 1 ? '🥈 2' : idx === 2 ? '🥉 3' : `#${idx + 1}`}
                    </td>
                    <td className="py-2.5 font-sans font-semibold">
                      <div>{p.name}</div>
                      <div className="text-[10px] text-slate-500 font-normal">{p.country}</div>
                    </td>
                    <td className="py-2.5 text-right text-slate-400">{p.totalMatches}</td>
                    <td className="py-2.5 text-right font-bold text-white text-sm">{p.totalWickets}</td>
                    <td className="py-2.5 text-right text-slate-300">{p.bestBowling}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};
