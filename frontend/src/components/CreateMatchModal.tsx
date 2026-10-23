import React, { useState } from 'react';
import { X, Trophy, Shield, Play } from 'lucide-react';
import { Team, MatchFormat, CreateMatchRequest } from '../types/cricket';

interface CreateMatchModalProps {
  teams: Team[];
  isOpen: boolean;
  onClose: () => void;
  onCreate: (req: CreateMatchRequest) => void;
}

export const CreateMatchModal: React.FC<CreateMatchModalProps> = ({
  teams,
  isOpen,
  onClose,
  onCreate
}) => {
  const [teamAId, setTeamAId] = useState<number>(teams[0]?.id || 1);
  const [teamBId, setTeamBId] = useState<number>(teams[1]?.id || 2);
  const [seriesName, setSeriesName] = useState<string>('Super Cricket League 2026');
  const [venue, setVenue] = useState<string>('Wankhede Stadium, Mumbai');
  const [format, setFormat] = useState<MatchFormat>('T20');
  const [totalOvers, setTotalOvers] = useState<number>(20);
  const [tossWinnerId, setTossWinnerId] = useState<number>(teams[0]?.id || 1);
  const [tossChoice, setTossChoice] = useState<string>('BAT');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (teamAId === teamBId) {
      alert('Please select two different teams for the match.');
      return;
    }

    const teamA = teams.find((t) => t.id === Number(teamAId));
    const teamB = teams.find((t) => t.id === Number(teamBId));
    const title = `${teamA?.name} vs ${teamB?.name}`;

    onCreate({
      title,
      seriesName,
      venue,
      format,
      totalOvers: Number(totalOvers),
      teamAId: Number(teamAId),
      teamBId: Number(teamBId),
      tossWinnerId: Number(tossWinnerId),
      tossChoice
    });

    onClose();
  };

  const handleFormatChange = (fmt: MatchFormat) => {
    setFormat(fmt);
    if (fmt === 'T20') setTotalOvers(20);
    else if (fmt === 'ODI') setTotalOvers(50);
    else if (fmt === 'BLITZ') setTotalOvers(10);
    else if (fmt === 'TEST') setTotalOvers(90);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-fade-in">
      <div className="w-full max-w-lg rounded-2xl bg-[#0E1528] border border-slate-700 shadow-2xl p-6 relative">
        <button
          onClick={onClose}
          className="absolute top-5 right-5 text-slate-400 hover:text-white p-1 rounded-lg hover:bg-slate-800 transition-all"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-2.5 mb-5">
          <div className="w-9 h-9 rounded-xl bg-emerald-500/20 text-emerald-400 flex items-center justify-center">
            <Trophy className="w-5 h-5" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-white">Create New Match</h3>
            <p className="text-xs text-slate-400">Configure teams, venue and rules</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4 text-xs">
          {/* Team Selectors */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Team A (Home)</label>
              <select
                value={teamAId}
                onChange={(e) => {
                  setTeamAId(Number(e.target.value));
                  setTossWinnerId(Number(e.target.value));
                }}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              >
                {teams.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.flagEmoji} {t.name} ({t.shortName})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Team B (Away)</label>
              <select
                value={teamBId}
                onChange={(e) => setTeamBId(Number(e.target.value))}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              >
                {teams.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.flagEmoji} {t.name} ({t.shortName})
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Series & Venue */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Tournament / Series</label>
              <input
                type="text"
                value={seriesName}
                onChange={(e) => setSeriesName(e.target.value)}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              />
            </div>
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Venue</label>
              <input
                type="text"
                value={venue}
                onChange={(e) => setVenue(e.target.value)}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              />
            </div>
          </div>

          {/* Format & Overs */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Match Format</label>
              <select
                value={format}
                onChange={(e) => handleFormatChange(e.target.value as MatchFormat)}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              >
                <option value="T20">T20 (20 Overs)</option>
                <option value="BLITZ">Blitz (10 Overs)</option>
                <option value="ODI">ODI (50 Overs)</option>
              </select>
            </div>
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Overs Per Innings</label>
              <input
                type="number"
                min="1"
                max="50"
                value={totalOvers}
                onChange={(e) => setTotalOvers(Number(e.target.value))}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              />
            </div>
          </div>

          {/* Toss */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Toss Winner</label>
              <select
                value={tossWinnerId}
                onChange={(e) => setTossWinnerId(Number(e.target.value))}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              >
                {teams.filter((t) => t.id === Number(teamAId) || t.id === Number(teamBId)).map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.flagEmoji} {t.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block font-semibold text-slate-300 mb-1.5">Toss Decision</label>
              <select
                value={tossChoice}
                onChange={(e) => setTossChoice(e.target.value)}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-white focus:outline-none focus:border-emerald-500"
              >
                <option value="BAT">Elected to BAT first</option>
                <option value="BOWL">Elected to BOWL first</option>
              </select>
            </div>
          </div>

          <div className="pt-3 flex items-center justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-800 transition-all font-semibold"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 py-2 rounded-xl bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 font-bold shadow-lg shadow-emerald-500/20 active:scale-95 transition-all"
            >
              Create Match
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
