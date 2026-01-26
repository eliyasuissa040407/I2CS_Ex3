package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;
import java.awt.Color;

public class Ex3Algo implements PacManAlgo {
    private Pixel2D _lastPos = null; // זיכרון של הצעד הקודם למניעת לופים

    @Override
    public String getInfo() {
        return "Level 4 Final: Smooth Movement & Dead-End Avoidance";
    }

    @Override
    public int move(PacmanGame game) {
        int[][] board = game.getGame(0);
        String posStr = game.getPos(0);
        if (board == null || posStr == null) return Game.STAY;

        String[] splitPos = posStr.split(",");
        Pixel2D pacmanPos = new Index2D(Integer.parseInt(splitPos[0]), Integer.parseInt(splitPos[1]));

        Map map = new Map(board);
        map.setCyclic(game.isCyclic());
        int wall = Game.getIntColor(Color.BLUE, 0);
        GhostCL[] ghosts = game.getGhosts(0);

        double minGhostDist = Double.MAX_VALUE;
        for (GhostCL g : ghosts) {
            if (g != null && g.remainTimeAsEatable(0) <= 0) {
                String[] gPos = g.getPos(0).split(",");
                Pixel2D ghostPos = new Index2D(Integer.parseInt(gPos[0]), Integer.parseInt(gPos[1]));
                minGhostDist = Math.min(minGhostDist, pacmanPos.distance2D(ghostPos));
            }
        }

        int nextDir;
        if (minGhostDist < 4.0) {
            nextDir = getSmartEscapeDir(pacmanPos, ghosts, map, wall);
        } else {
            Map2D distMap = map.allDistance(pacmanPos, wall);
            Pixel2D target = findClosest(distMap, board, Game.getIntColor(Color.GREEN, 0));
            if (target == null) target = findClosest(distMap, board, Game.getIntColor(Color.PINK, 0));

            if (target == null) nextDir = Game.STAY;
            else nextDir = nextStep(pacmanPos, target, map);
        }

        _lastPos = pacmanPos;
        return nextDir;
    }

    private int getSmartEscapeDir(Pixel2D pacman, GhostCL[] ghosts, Map map, int wall) {
        int[] dirs = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};
        int bestDir = Game.STAY;
        double maxScore = -1000;

        for (int dir : dirs) {
            Pixel2D neighbor = getNeighbor(pacman, dir, map);

            if (map.isInside(neighbor) && map.getPixel(neighbor) != wall) {
                if (_lastPos != null && neighbor.equals(_lastPos)) continue;

                double minDistToGhost = Double.MAX_VALUE;
                for (GhostCL g : ghosts) {
                    if (g != null && g.remainTimeAsEatable(0) <= 0) {
                        String[] gPos = g.getPos(0).split(",");
                        Pixel2D ghostPos = new Index2D(Integer.parseInt(gPos[0]), Integer.parseInt(gPos[1]));
                        minDistToGhost = Math.min(minDistToGhost, neighbor.distance2D(ghostPos));
                    }
                }

                int options = countOptions(neighbor, map, wall);
                double score = minDistToGhost * 2.0 + options;
                if (options <= 1) score -= 10; // קנס כבד למבוי סתום

                if (score > maxScore) {
                    maxScore = score;
                    bestDir = dir;
                }
            }
        }

        if (bestDir == Game.STAY && _lastPos != null) {
            _lastPos = null;
            return getSmartEscapeDir(pacman, ghosts, map, wall);
        }

        return bestDir;
    }

    private int countOptions(Pixel2D p, Map map, int wall) {
        int count = 0;
        int[] dirs = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};
        for (int dir : dirs) {
            Pixel2D n = getNeighbor(p, dir, map);
            if (map.isInside(n) && map.getPixel(n) != wall) count++;
        }
        return count;
    }

    private Pixel2D findClosest(Map2D distMap, int[][] board, int color) {
        Pixel2D closest = null;
        int minDist = Integer.MAX_VALUE;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == color) {
                    int d = distMap.getPixel(x, y);
                    if (d > 0 && d < minDist) {
                        minDist = d;
                        closest = new Index2D(x, y);
                    }
                }
            }
        }
        return closest;
    }

    private int nextStep(Pixel2D current, Pixel2D target, Map map) {
        int[] directions = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};
        int bestDir = Game.STAY;
        int wall = Game.getIntColor(Color.BLUE, 0);
        Map2D distMapFromTarget = map.allDistance(target, wall);
        int minDist = Integer.MAX_VALUE;
        for (int dir : directions) {
            Pixel2D neighbor = getNeighbor(current, dir, map);
            if (map.isInside(neighbor) && map.getPixel(neighbor) != wall) {
                int d = distMapFromTarget.getPixel(neighbor);
                if (d != -1 && d < minDist) {
                    minDist = d;
                    bestDir = dir;
                }
            }
        }
        return bestDir;
    }

    private Pixel2D getNeighbor(Pixel2D p, int dir, Map map) {
        int x = p.getX(), y = p.getY();
        if (dir == Game.UP) y++;
        else if (dir == Game.DOWN) y--;
        else if (dir == Game.LEFT) x--;
        else if (dir == Game.RIGHT) x++;
        if (map.isCyclic()) {
            x = (x + map.getWidth()) % map.getWidth();
            y = (y + map.getHeight()) % map.getHeight();
        }
        return new Index2D(x, y);
    }
}