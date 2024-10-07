# Agar.cdp - A Distributed and Concurrent Game
![image](https://github.com/user-attachments/assets/ba5ceff6-5559-4db8-9d1e-dc3fda247e30)





## Overview
Agar.pcd is a simplified version of the popular online game [Agar.io](https://pt.wikipedia.org/wiki/Agar.io), the goal of this project is to apply concepts of concurrency and distributed systems.
Players compete in a distributed setting, absorbing energy from opponents to reach the maximum energy level of 10.

## Features
- **Concurrent Gameplay**: Players, both human-controlled and automated, compete in real-time within the same game environment.
- **Distributed System**: Players are implemented as remote applications, connecting to a central game server.
- **Energy-based Confrontation**: Players gain energy from confronting others, with the player having more energy winning the confrontation.
- **Randomly Moving Automated Players**: Automated players move randomly and can interact with human players. 
- **Game End Condition**: The game ends successfully when three players reach the maximum energy level of 10.

## Game Rules
1. **Objective**: The goal is to absorb energy from other players and reach the maximum energy level of 10. Once a player reaches this level, they become inactive and are represented by an 'X' on the GUI.
2. **Confrontation**: A confrontation occurs when a player moves to a cell occupied by another player. The player with the higher energy wins and absorbs the energy of the defeated player. If the energy levels are similar, the outcome is random with equal probability for both players.
3. **Movement Limitations**: Each player can move based on their initial energy level:
   - A player with an initial energy of 1 can move every turn.
   - A player with an initial energy of 3 can only move once every three turns.
   These movement limitations remain consistent throughout the game, regardless of current energy levels.
4. **Inactivity and Obstacles**: Defeated players become immovable obstacles on the board, which active players must navigate around.
5. **Automated Players**: Automated players use random movements within the game board. 
6. **Game End**: The game ends successfully when three players reach the maximum energy level of 10.

## Technical Requirements
- **Concurrency**: The game requires precise handling of concurrent access to shared resources, including player positions and energy levels. This is achieved using synchronization mechanisms to ensure safe interactions.
- **Distribution**: Human players are implemented as remote clients connecting to a central server. The server periodically broadcasts the game state to all connected clients, while clients only send their intended movement direction upon user input.
- **Synchronization**: Critical sections of code are protected using appropriate synchronization techniques to ensure mutual exclusion and consistent game state.

## Setup and Installation
1. **Clone the Repository**:
   ```bash
   git clone git@github.com:SilvaSalvador/Agar.cdp.git
2. **Build the Project**: Ensure you have the necessary build tools and dependencies installed. Compile the code using your preferred method (e.g., sbt, Maven, etc.).

3. **Run the Server**: Start the game server, which will manage all the game logic and communicate with remote clients.

4. **Run Clients**: Launch human or automated players as separate processes, connecting them to the server using the appropriate configurations.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
