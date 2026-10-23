import React from 'react';
import { Shield, Zap, CircleDot, User, Flame } from 'lucide-react';
import { Innings, CricketMatch } from '../types/cricket';

interface LiveActivePlayersProps {
  match: CricketMatch;
}

export const LiveActivePlayers: React.FC<LiveActivePlayersProps> = ({ match }) => {
  const currentInnings = match.currentInningsNumber === 1 ? match.firstInnings : match.secondInnings;

  if (!currentInnings) {
    return null;
  }

  const activeBatsmen = currentInnings.batsmen?.filter((b) => b.batting && !b.out) || [];
  const striker = activeBatsmen.find((b) => b.onStrike) || activeBatsmen[0];
  const nonStriker = activeBatsmen.find((b) => !b.onStrike && b !== striker) || activeBatsmen[1];
  const activeBowler = currentInnings.bowlers?.find((bw) => bw.current) || currentInnings.bowlers?.[0];

  const partnershipRuns = (striker?.runs || 0) + (nonStriker?.runs || 0);
  const partnershipBalls = (striker?.balls || 0) + (nonStriker?.balls || 0);

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
      {/* Batting Crease Card (2 columns wide) */}
      <div className="lg:col-span-2 rounded-2xl bg-[#0E1528] border border-slate-800/80 p-5 shadow-lg">
        <div className="flex items-center justify-between border-b border-slate-800 pb-3 mb-4">
          <div className="flex items-center gap-2">
            <div className="w-2.5 h-2.5 rounded-full bg-emerald-400 animate-pulse" />
            <h3 className="text-sm font-bold uppercase tracking-wider text-slate-200">
              Batting Crease ({currentInnings.battingTeamName})
            </h3>
          </div>
          <div className="text-xs font-mono text-slate-400 bg-slate-800/60 px-2.5 py-1 rounded-md">
            Partnership: <strong className="text-emerald-400">{partnershipRuns}</strong> ({partnershipBalls}b)
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="text-slate-400 border-b border-slate-800/60 pb-2">
                <th className="font-semibold py-2">Batter</th>
                <th className="font-semibold text-right py-2">R</th>
                <th className="font-semibold text-right py-2">B</th>
                <th className="font-semibold text-right py-2">4s</th>
                <th className="font-semibold text-right py-2">6s</th>
                <th className="font-semibold text-right py-2">SR</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/40">
              {striker && (
                <tr className="text-slate-200 bg-emerald-950/20 font-medium">
                  <td className="py-2.5 flex items-center gap-2">
                    <span className="text-emerald-400 font-bold text-sm">🏏</span>
                    <span className="font-bold text-white text-sm">{striker.playerName}</span>
                    <span className="text-[10px] bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 px-1.5 py-0.2 rounded font-mono font-bold">
                      STRIKE
                    </span>
                  </td>
                  <td className="text-right py-2.5 font-mono text-base font-bold text-emerald-400">
                    {striker.runs}
                  </td>
                  <td className="text-right py-2.5 font-mono text-slate-400">{striker.balls}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{striker.fours}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{striker.sixes}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{striker.strikeRate}</td>
                </tr>
              )}

              {nonStriker && (
                <tr className="text-slate-200">
                  <td className="py-2.5 flex items-center gap-2">
                    <span className="text-slate-600 font-bold text-sm">🏏</span>
                    <span className="font-semibold text-slate-300 text-sm">{nonStriker.playerName}</span>
                  </td>
                  <td className="text-right py-2.5 font-mono text-base font-bold text-slate-200">
                    {nonStriker.runs}
                  </td>
                  <td className="text-right py-2.5 font-mono text-slate-400">{nonStriker.balls}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{nonStriker.fours}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{nonStriker.sixes}</td>
                  <td className="text-right py-2.5 font-mono text-slate-300">{nonStriker.strikeRate}</td>
                </tr>
              )}

              {!striker && !nonStriker && (
                <tr>
                  <td colSpan={6} className="text-center py-4 text-slate-500 italic">
                    Innings not in progress or all out
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Bowling Card (1 column wide) */}
      <div className="rounded-2xl bg-[#0E1528] border border-slate-800/80 p-5 shadow-lg flex flex-col justify-between">
        <div>
          <div className="flex items-center justify-between border-b border-slate-800 pb-3 mb-4">
            <div className="flex items-center gap-2">
              <Zap className="w-4 h-4 text-amber-400" />
              <h3 className="text-sm font-bold uppercase tracking-wider text-slate-200">
                Current Bowler
              </h3>
            </div>
            <span className="text-xs font-mono text-slate-400">
              {currentInnings.bowlingTeamName}
            </span>
          </div>

          {activeBowler ? (
            <div>
              <div className="flex items-center justify-between mb-3">
                <span className="font-bold text-base text-white">{activeBowler.playerName}</span>
                <span className="text-[10px] bg-amber-500/20 text-amber-300 border border-amber-500/30 px-2 py-0.5 rounded font-mono font-bold">
                  BOWLING
                </span>
              </div>

              <div className="grid grid-cols-4 gap-2 text-center bg-slate-900/60 p-3 rounded-xl border border-slate-800/60">
                <div>
                  <div className="text-[11px] text-slate-400 font-semibold">Overs</div>
                  <div className="text-base font-bold font-mono text-white">{activeBowler.overs}</div>
                </div>
                <div>
                  <div className="text-[11px] text-slate-400 font-semibold">M</div>
                  <div className="text-base font-bold font-mono text-slate-300">{activeBowler.maidens}</div>
                </div>
                <div>
                  <div className="text-[11px] text-slate-400 font-semibold">Runs</div>
                  <div className="text-base font-bold font-mono text-slate-300">{activeBowler.runsConceded}</div>
                </div>
                <div>
                  <div className="text-[11px] text-rose-400 font-semibold">Wkts</div>
                  <div className="text-base font-bold font-mono text-rose-400">{activeBowler.wickets}</div>
                </div>
              </div>

              <div className="mt-3 flex items-center justify-between text-xs text-slate-400 font-mono px-1">
                <span>Economy: <strong className="text-cyan-300">{activeBowler.economy}</strong></span>
                <span>Wides: <strong className="text-amber-300">{activeBowler.wides}</strong> | NB: <strong className="text-amber-300">{activeBowler.noBalls}</strong></span>
              </div>
            </div>
          ) : (
            <div className="text-center py-6 text-slate-500 italic text-xs">
              No bowler active
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
