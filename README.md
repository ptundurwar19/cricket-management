# 🏏 CricPulse - Scalable Real-Time Cricket Score Management System

A high-performance, scalable, real-time cricket score management and match simulation platform built with **Spring Boot 3 (Java)** on the backend and **React + TypeScript + Tailwind CSS** on the frontend.

---

## 🌟 Key Highlights & Features

### 1. ⚡ Real-Time WebSocket & Server-Sent Events (SSE) Engine
- **Spring WebSocket with STOMP & SockJS**: Instant sub-millisecond push updates to all connected browser clients on ball delivery.
- **Bi-directional Topics**:
  - `/topic/matches`: Broadcasts match state updates across the tournament.
  - `/topic/matches/{matchId}`: High-frequency live score, ball events, and commentary channel for focused matches.
- **Resilient Fallback**: Automatic connection management with SSE stream (`/api/stream/live`) and reconnect logic.

### 2. 🎯 Probabilistic Match Simulator
- **Cricket Physics & Probability Engine**: Realistic ball simulation factoring in batsman aggression, bowler skill, format pace (T20 vs ODI), and match situations.
- **Accurate Cricket Rules**:
  - Strike rotation on odd runs (1, 3) and at over boundaries (every 6 legal balls).
  - Bowler rotation (preventing consecutive overs by the same bowler).
  - Extra handling: Wides, No-Balls, Byes, and Leg-Byes.
  - Wicket mechanisms: Bowled, Caught, LBW, Run Out, Stumped.
  - Innings transition & Target calculation (`target = 1st_innings_runs + 1`).
  - Target chase logic: Real-time Required Run Rate (RRR), Current Run Rate (CRR), balls remaining, and victory conditions (win by runs, win by wickets, tie).
- **Simulation Controls**: Play, Pause, Resume, Reset, and configurable simulation speeds (1x, 2x, 4x).

### 3. 📋 Official Umpire / Scorer Console
- Manual ball-by-ball scoring interface allowing umpires and scorers to log:
  - Exact runs (0, 1, 2, 3, 4, 6)
  - Extras (Wide, No Ball, Bye, Leg Bye)
  - Dismissals (Wicket type, fielder/bowler attribution)
  - Custom broadcast commentary notes

### 4. 📊 Detailed Scorecards & Ball-by-Ball Commentary
- **Innings Scorecards**: Tabbed batting tables (Runs, Balls, 4s, 6s, Strike Rate, dismissal status) and bowling figures (Overs, Maidens, Runs, Wickets, Economy).
- **Fall of Wickets**: Instant tracking of wicket fall chronology with score and over.
- **Live Commentary Feed**: Dynamic, broadcast-style prose with filter chips ("All Deliveries", "Boundaries Only", "Wickets Only").
- **Recent Balls Tracker**: Color-coded visual badges (🟢 4/6, 🔴 Wicket, 🔵 Singles, ⚪ Dots, 🟡 Extras).

### 5. 🏆 Tournament Leaderboard & Player Stats
- **Orange Cap**: Top tournament run scorers, highest individual score, matches played.
- **Purple Cap**: Leading wicket takers, best bowling figures, economy rates.

---

## 🏗️ Architecture

```mermaid
flowchart TD
    Client[React + TypeScript Frontend\nTailwind CSS + Lucide Icons]
    
    subgraph SpringBoot [Spring Boot Backend]
        Controller[REST Controllers\n/api/matches, /api/teams, /api/players]
        Broker[STOMP WebSocket Message Broker\n/topic/matches, /topic/matches/{id}]
        SSE[SSE Stream Emitter\n/api/stream/live]
        SimEngine[Simulation Engine & Rules Validator]
        CommGen[Dynamic Commentary Generator]
        JPA[Spring Data JPA Repositories]
        DB[(H2 In-Memory Database)]
    end

    Client -->|REST API Calls| Controller
    Client <-->|WebSocket STOMP / SockJS| Broker
    Client -->|Fallback EventSource| SSE
    Controller --> SimEngine
    SimEngine --> CommGen
    SimEngine --> JPA
    SimEngine --> Broker
    SimEngine --> SSE
    JPA --> DB
```

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java 17, 21, or 26**
- **Node.js 18+ & npm**

### 1. Start Spring Boot Backend
```bash
cd backend
# Using Maven wrapper (included)
.\mvnw.cmd spring-boot:run
# Or run pre-built JAR directly:
java -jar target/cricket-score-management-1.0.0.jar
```
*Backend starts on `http://localhost:8080`.*
*H2 Console accessible at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:cricketdb`, User: `sa`, Password: empty).*

### 2. Start Frontend Dashboard
```bash
cd frontend
npm install
npm run dev
```
*Frontend opens at `http://localhost:3000` with automatic proxy to the Spring Boot backend.*

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/matches` | Get all matches (optional `?status=LIVE\|UPCOMING\|COMPLETED`) |
| `GET` | `/api/matches/{id}` | Get full match details including both innings |
| `POST` | `/api/matches` | Create a new match between two teams |
| `POST` | `/api/matches/{id}/start` | Start live match simulation (`?speedMs=1500`) |
| `POST` | `/api/matches/{id}/pause` | Pause live match simulation |
| `POST` | `/api/matches/{id}/resume` | Resume live match simulation |
| `POST` | `/api/matches/{id}/reset` | Reset match to initial state |
| `POST` | `/api/matches/{id}/ball` | Score a single ball (manual or step) |
| `GET` | `/api/matches/{id}/commentary` | Get ball-by-ball commentary feed |
| `GET` | `/api/matches/leaderboard` | Get Orange & Purple Cap leaderboards |
| `GET` | `/api/teams` | Get all international teams & rosters |
| `GET` | `/api/players` | Get player profiles and stats |
| `GET` | `/api/stream/live` | Server-Sent Events (SSE) live updates stream |

### WebSocket STOMP Endpoints
- **Connection URL**: `http://localhost:8080/ws` (SockJS enabled) or `ws://localhost:8080/ws-native`
- **Subscribed Channels**:
  - `/topic/matches`: Global live score broadcasts
  - `/topic/matches/{matchId}`: Specific match updates

---

## 🧪 Pre-Configured Seed Matches

When the backend starts, the following tournament matches are pre-seeded and ready to explore:
1. **India vs Australia - World Cup Final 2026** (T20, Live in 2nd Innings! Australia set 176/6. India in an electric chase at 132/3 in 14.3 overs with Virat Kohli & Hardik Pandya at the crease).
2. **England vs Australia - The Ashes T20 Classic** (Upcoming at Lord's, ready to start).
3. **India vs South Africa - Championship Thriller** (Completed final with full scorecard and 7-run victory margin).
