import React, { useState } from 'react';
import { CricketMatch, Innings } from '../types/cricket';

interface ScorecardViewProps {
  match: CricketMatch;
}

export const ScorecardView: React.FC<ScorecardViewProps> = ({ match }) => {
  const [selectedInningsNum, setSelectedInningsNum] = useState<number>(
    match.currentInningsNumber || 1
  );

  const selectedInnings: Innings | undefined =
    selectedInningsNum === 1 ? match.firstInnings : match.secondInnings;

  return (
    <div className="rounded-2xl bg-[#0E1528] border border-slate-800/80 p-5 shadow-xl">
      {/* Innings Tabs */}
      <div className="flex items-center justify-between border-b border-slate-800 pb-3 mb-5">
        <div className="flex items-center gap-2">
          {match.firstInnings && (
            <button
              onClick={() => setSelectedInningsNum(1)}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                selectedInningsNum === 1
                  ? 'bg-slate-800 text-emerald-400 border border-emerald-500/30 shadow-md'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              1st Innings: {match.firstInnings.battingTeamName} ({match.firstInnings.totalRuns}/{match.firstInnings.totalWickets})
            </button>
          )}

          {match.secondInnings && (
            <button
              onClick={() => setSelectedInningsNum(2)}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                selectedInningsNum === 2
                  ? 'bg-slate-800 text-emerald-400 border border-emerald-500/30 shadow-md'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              2nd Innings: {match.secondInnings.battingTeamName} ({match.secondInnings.totalRuns}/{match.secondInnings.totalWickets})
            </button>
          )}
        </div>

        {selectedInnings && (
          <div className="text-xs font-mono text-slate-400 hidden sm:block">
            CRR: <strong className="text-emerald-400">{selectedInnings.oversBowled > 0 ? (Math.round((selectedInnings.totalRuns / (selectedInnings.legalBallsCount / 6.0)) * 100) / 100) : 0}</strong>
          </div>
        )}
      </div>

      {!selectedInnings ? (
        <div className="text-center py-10 text-slate-500 italic text-sm">
          Innings scorecard data not available yet
        </div>
      ) : (
        <div className="space-y-6">
          {/* Batting Table */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3 flex items-center gap-2">
              <span>Batting</span>
              <span className="text-slate-600">•</span>
              <span className="text-slate-300">{selectedInnings.battingTeamName}</span>
            </h4>
            <div className="overflow-x-auto rounded-xl border border-slate-800/80 bg-slate-900/40">
              <table className="w-full text-left text-xs">
                <thead>
                  <tr className="border-b border-slate-800 text-slate-400 bg-slate-900/80">
                    <th className="py-2.5 px-4 font-semibold">Batter</th>
                    <th className="py-2.5 px-4 font-semibold">Dismissal</th>
                    <th className="py-2.5 px-3 text-right font-semibold">R</th>
                    <th className="py-2.5 px-3 text-right font-semibold">B</th>
                    <th className="py-2.5 px-3 text-right font-semibold">4s</th>
                    <th className="py-2.5 px-3 text-right font-semibold">6s</th>
                    <th className="py-2.5 px-4 text-right font-semibold">SR</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/40 font-mono">
                  {selectedInnings.batsmen?.map((b) => (
                    <tr
                      key={b.id || b.playerName}
                      className={b.batting && !b.out ? 'bg-emerald-950/20 text-slate-100 font-semibold' : 'text-slate-300'}
                    >
                      <td className="py-2.5 px-4 font-sans font-semibold flex items-center gap-2">
                        {b.batting && !b.out && <span className="text-emerald-400">🏏</span>}
                        <span>{b.playerName}</span>
                        {b.onStrike && (
                          <span className="text-[9px] bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 px-1 py-0.2 rounded font-sans">
                            *
                          </span>
                        )}
                      </td>
                      <td className="py-2.5 px-4 text-slate-400 font-sans text-xs">
                        {b.out ? b.dismissalInfo : b.batting ? 'batting' : 'yet to bat'}
                      </td>
                      <td className="py-2.5 px-3 text-right font-bold text-white text-sm">{b.runs}</td>
                      <td className="py-2.5 px-3 text-right text-slate-400">{b.balls}</td>
                      <td className="py-2.5 px-3 text-right text-slate-300">{b.fours}</td>
                      <td className="py-2.5 px-3 text-right text-slate-300">{b.sixes}</td>
                      <td className="py-2.5 px-4 text-right text-slate-300">{b.strikeRate}</td>
                    </tr>
                  ))}
                  {/* Extras Row */}
                  <tr className="bg-slate-900/60 font-sans text-slate-400">
                    <td colSpan={2} className="py-2.5 px-4 font-semibold">
                      Extras
                    </td>
                    <td colSpan={5} className="py-2.5 px-4 text-right font-mono font-bold text-slate-200">
                      {selectedInnings.totalExtras} (w {selectedInnings.wides}, nb {selectedInnings.noBalls}, b {selectedInnings.byes}, lb {selectedInnings.legByes})
                    </td>
                  </tr>
                  {/* Total Row */}
                  <tr className="bg-slate-900/90 font-sans text-white font-bold border-t border-slate-700">
                    <td colSpan={2} className="py-3 px-4 text-sm">
                      Total
                    </td>
                    <td colSpan={5} className="py-3 px-4 text-right font-mono text-base text-emerald-400">
                      {selectedInnings.totalRuns} / {selectedInnings.totalWickets}{' '}
                      <span className="text-xs text-slate-400 font-normal">
                        ({selectedInnings.oversBowled} ov, RR: {selectedInnings.oversBowled > 0 ? (Math.round((selectedInnings.totalRuns / (selectedInnings.legalBallsCount / 6.0)) * 100) / 100) : 0})
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Bowling Table */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3 flex items-center gap-2">
              <span>Bowling</span>
              <span className="text-slate-600">•</span>
              <span className="text-slate-300">{selectedInnings.bowlingTeamName}</span>
            </h4>
            <div className="overflow-x-auto rounded-xl border border-slate-800/80 bg-slate-900/40">
              <table className="w-full text-left text-xs font-mono">
                <thead>
                  <tr className="border-b border-slate-800 text-slate-400 bg-slate-900/80 font-sans">
                    <th className="py-2.5 px-4 font-semibold">Bowler</th>
                    <th className="py-2.5 px-3 text-right font-semibold">O</th>
                    <th className="py-2.5 px-3 text-right font-semibold">M</th>
                    <th className="py-2.5 px-3 text-right font-semibold">R</th>
                    <th className="py-2.5 px-3 text-right font-semibold">W</th>
                    <th className="py-2.5 px-4 text-right font-semibold">ECON</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/40">
                  {selectedInnings.bowlers?.map((bw) => (
                    <tr
                      key={bw.id || bw.playerName}
                      className={bw.current ? 'bg-amber-950/20 text-slate-100 font-semibold' : 'text-slate-300'}
                    >
                      <td className="py-2.5 px-4 font-sans font-semibold flex items-center gap-2">
                        {bw.current && <span className="text-amber-400 text-xs">⚡</span>}
                        <span>{bw.playerName}</span>
                      </td>
                      <td className="py-2.5 px-3 text-right text-slate-200">{bw.overs}</td>
                      <td className="py-2.5 px-3 text-right text-slate-400">{bw.maidens}</td>
                      <td className="py-2.5 px-3 text-right text-slate-200">{bw.runsConceded}</td>
                      <td className="py-2.5 px-3 text-right font-bold text-rose-400">{bw.wickets}</td>
                      <td className="py-2.5 px-4 text-right text-cyan-300">{bw.economy}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Fall of Wickets */}
          {selectedInnings.fallOfWickets && selectedInnings.fallOfWickets.length > 0 && (
            <div>
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
                Fall of Wickets
              </h4>
              <div className="flex items-center gap-3 flex-wrap">
                {selectedInnings.fallOfWickets.map((fow) => (
                  <div
                    key={fow.id || fow.wicketNumber}
                    className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800 text-xs font-mono"
                  >
                    <span className="text-rose-400 font-bold">{fow.scoreAtFall}/{fow.wicketNumber}</span>{' '}
                    <span className="text-slate-400">({fow.batsmanName}, {fow.overAtFall} ov)</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
