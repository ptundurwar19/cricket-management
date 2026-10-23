import { Client } from '@stomp/stompjs';
import { LiveScoreUpdateDTO } from '../types/cricket';

type ScoreUpdateCallback = (update: LiveScoreUpdateDTO) => void;
type ConnectionStatusCallback = (status: 'connected' | 'disconnected' | 'connecting') => void;

class RealtimeScoreClient {
  private stompClient: Client | null = null;
  private sseSource: EventSource | null = null;
  private subscribers: Set<ScoreUpdateCallback> = new Set();
  private statusListeners: Set<ConnectionStatusCallback> = new Set();
  private activeMatchId: number | null = null;
  private status: 'connected' | 'disconnected' | 'connecting' = 'disconnected';

  public connect(matchId?: number) {
    this.activeMatchId = matchId || null;
    this.updateStatus('connecting');

    try {
      this.initStomp();
    } catch (e) {
      console.warn('Native WebSocket/STOMP initialization failed, falling back to SSE:', e);
      this.initSse();
    }
  }

  private initStomp() {
    const isHttps = typeof window !== 'undefined' && window.location.protocol === 'https:';
    const wsProto = isHttps ? 'wss:' : 'ws:';
    const host = typeof window !== 'undefined' ? window.location.host : 'localhost:3000';
    const brokerURL = `${wsProto}//${host}/ws-native`;

    this.stompClient = new Client({
      brokerURL: brokerURL,
      reconnectDelay: 4000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        this.updateStatus('connected');
        // Global feed
        this.stompClient?.subscribe('/topic/matches', (message) => {
          try {
            const update: LiveScoreUpdateDTO = JSON.parse(message.body);
            this.notifySubscribers(update);
          } catch (err) {
            console.error('Error parsing live update:', err);
          }
        });

        // Match-specific feed
        if (this.activeMatchId) {
          this.subscribeToMatch(this.activeMatchId);
        }
      },
      onDisconnect: () => {
        this.updateStatus('disconnected');
      },
      onStompError: (frame) => {
        console.warn('STOMP Broker error: ' + frame.headers['message']);
        this.updateStatus('disconnected');
        this.initSse(); // Fallback to SSE
      },
      onWebSocketClose: () => {
        if (this.status === 'connected') {
          this.updateStatus('connecting');
        }
      },
      onWebSocketError: () => {
        // Fallback to SSE if WebSocket connection refused
        this.initSse();
      }
    });

    this.stompClient.activate();
  }

  private initSse() {
    if (this.sseSource) {
      this.sseSource.close();
    }

    try {
      this.sseSource = new EventSource('/api/stream/live');
      this.sseSource.addEventListener('score-update', (event) => {
        try {
          const update: LiveScoreUpdateDTO = JSON.parse(event.data);
          this.notifySubscribers(update);
          this.updateStatus('connected');
        } catch (e) {
          console.error('Failed to parse SSE payload:', e);
        }
      });

      this.sseSource.onopen = () => {
        this.updateStatus('connected');
      };

      this.sseSource.onerror = () => {
        this.updateStatus('disconnected');
      };
    } catch (e) {
      console.warn('SSE fallback also not supported:', e);
    }
  }

  public setFocusedMatch(matchId: number) {
    this.activeMatchId = matchId;
    if (this.stompClient && this.stompClient.connected) {
      this.subscribeToMatch(matchId);
    }
  }

  private subscribeToMatch(matchId: number) {
    try {
      this.stompClient?.subscribe(`/topic/matches/${matchId}`, (message) => {
        try {
          const update: LiveScoreUpdateDTO = JSON.parse(message.body);
          this.notifySubscribers(update);
        } catch (err) {
          console.error('Error parsing match update:', err);
        }
      });
    } catch (e) {
      console.warn('Error subscribing to match topic:', e);
    }
  }

  public subscribe(cb: ScoreUpdateCallback): () => void {
    this.subscribers.add(cb);
    return () => this.subscribers.delete(cb);
  }

  public onStatusChange(cb: ConnectionStatusCallback): () => void {
    this.statusListeners.add(cb);
    cb(this.status);
    return () => this.statusListeners.delete(cb);
  }

  private notifySubscribers(update: LiveScoreUpdateDTO) {
    this.subscribers.forEach((cb) => cb(update));
  }

  private updateStatus(newStatus: 'connected' | 'disconnected' | 'connecting') {
    this.status = newStatus;
    this.statusListeners.forEach((cb) => cb(newStatus));
  }

  public disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
    if (this.sseSource) {
      this.sseSource.close();
      this.sseSource = null;
    }
    this.updateStatus('disconnected');
  }
}

export const realtimeClient = new RealtimeScoreClient();
