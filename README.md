# I2CS_Ex3 - Pac-Man "Going Places"

## Algorithm Design (Level 4 Focus)
The core of this implementation is designed to survive the high-speed and aggressive ghost behavior found in Level 4.

### Strategies Used:
1. **Dynamic Heatmap**: Each cell on the board is scored based on:
   - **Ghost Proximity**: Using an exponential decay formula (100/d²), closer ghosts impose a massive penalty.
   - **Dead-End Avoidance**: Cells leading to corridors with only one exit receive a penalty to prevent being trapped.
   - **Reward Priority**: Green dots (power-ups) are prioritized over pink dots to neutralize threats.
2. **BFS Pathfinding**: When ghosts are at a safe distance (>8 units), the algorithm uses a Breadth-First Search (BFS) to find the shortest path to the nearest food.
3. **Anti-Vibration Logic**: A short-term memory buffer stores the previous position to prevent the Pac-Man from stuttering between two equally "safe" cells.

### How to Run:
1. Load the project in IntelliJ.
2. Run `Ex3Main.java`.
3. Select Level 4 to see the strategy in action.
