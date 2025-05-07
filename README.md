# 🃏 Multiplayer Blackjack Game

This project is a multiplayer Blackjack game developed in Java using a client-server architecture over TCP/IP. It supports account creation, table joining, betting, and maintains player data using a file-based CSV system. The game is played through a custom-built Swing GUI.

---

## ✅ Features Implemented

| Feature                         | Status   | Description                                                                 |
|---------------------------------|----------|-----------------------------------------------------------------------------|
| **Login / Account Creation**    | ✅ Done   | Players and dealers authenticate via `users.csv` and `dealers.csv`          |
| **GUI Prototypes**              | ✅ Done   | Login screen, table lobby, game table (player & dealer), betting window     |
| **Table Join / Create**         | ✅ Done   | Players can join tables; dealers can create new tables                      |
| **Betting System**              | ✅ Done   | Bet entry UI connected to server; wager validation and storage implemented  |
| **CSV-Based Database**          | ✅ Done   | Data stored in structured CSVs: accounts, sessions, history, leaderboard    |
| **Server-Client Communication** | ✅ Done   | TCP/IP with multithreaded request handling                                  |
| **Player History Logging**      | ✅ Done   | Each player’s session is logged in per-user CSVs inside `allPlayers/`       |

---

## ❌ Features Not Yet Implemented

| Feature              | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| **Game Logic**        | Card dealing, turn rotation, hand comparison, and payout logic incomplete   |
| **Turn Timer**        | No enforcement of timed player turns or auto-pass                          |
| **Leaderboard Display** | Leaderboard data is stored but not rendered in the GUI                    |

---

## 📁 Folder Structure
```text
CS401-BlackJack-Implementation/
├── res/
│   └── db/
│       ├── users.csv
│       ├── dealers.csv
│       ├── allPlayers/
│       │   ├── basim_funds.csv
│       │   └── basim_history.csv
│       └── players/
│           └── basim/
│               ├── funds.csv
│               └── history.csv
├── src/
│   ├── client/
│   │   ├── DealerLobbyGUI/
│   │   ├── DealerTable/
│   │   ├── gui/
│   │   ├── PlayerLobbyGUI/
│   │   ├── PlayerTable/
│   │   ├── test_gui/
│   ├── dbHelper/
│   ├── game/
│   ├── networking/
│   ├── server/
│   └── tests/
├── .idea/
├── README.md
└── .classpath / .project / etc.
```
---

## 💻 How to Run

1. **Clone the Repository**
   git clone https://github.com/CSUEB-Black-Jack-Group-5/CS401-BlackJack.git
   cd CS401-BlackJack
2. **Start the Server**
     Run Server.java (ensure port is open and not in use).
4. **Launch the Client**
     Run BlackjackGame.java
     Use login screen to enter as a player or dealer.

---

## 👥 Contributors
1. Riley Fischer
2. Martin Garcia
3. Michelle Nguyen
4. John Nguyen
5. Basim Shahzad

---

## 📌 Notes
1. This project uses no external database—only local file storage via CSV.
2. All game data is stored and read using Java File I/O.
3. Game logic and leaderboard integration were planned but not fully implemented due to time constraints.



   

