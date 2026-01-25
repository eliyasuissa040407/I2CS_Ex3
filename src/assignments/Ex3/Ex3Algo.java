package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;
import java.awt.Color;

public class Ex3Algo implements PacManAlgo {
    @Override
    public String getInfo() {
        return "BFS Algorithm for Pacman - Finding the shortest path to food.";
    }

    @Override
    public int move(PacmanGame game) {
        int[][] board = game.getGame(0);
        String posStr = game.getPos(0);
        if (board == null || posStr == null) return Game.STAY;

        // המרת המיקום של הפקמן
        String[] splitPos = posStr.split(",");
        Pixel2D pacmanPos = new Index2D(Integer.parseInt(splitPos[0]), Integer.parseInt(splitPos[1]));

        Map map = new Map(board);
        map.setCyclic(game.isCyclic());

        int wall = Game.getIntColor(Color.BLUE, 0);
        int pink = Game.getIntColor(Color.PINK, 0);
        int green = Game.getIntColor(Color.GREEN, 0);

        // מציאת הנקודה הקרובה ביותר
        Map2D distMap = map.allDistance(pacmanPos, wall);
        Pixel2D target = findClosest(distMap, board, green);
        if (target == null) target = findClosest(distMap, board, pink);

        if (target == null) return Game.STAY;
        return nextStep(pacmanPos, target, map);
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
        int bestDir = Game.UP;
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