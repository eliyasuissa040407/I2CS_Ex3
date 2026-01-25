package assignments.Ex3;

import java.util.LinkedList;
import java.util.Queue;

public class Map implements Map2D {
    private int[][] _map;
    private boolean _cyclic = true;

    public Map(int[][] m) { init(m); }

    @Override
    public void init(int w, int h, int v) {
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = v;
            }
        }
    }

    @Override
    public void init(int[][] arr) {
        if (arr == null || arr.length == 0) throw new RuntimeException("Array is invalid");
        _map = new int[arr.length][arr[0].length];
        for (int x = 0; x < arr.length; x++) {
            for (int y = 0; y < arr[0].length; y++) {
                _map[x][y] = arr[x][y];
            }
        }
    }

    @Override
    public int[][] getMap() {
        // החזרת עותק עמוק כדי לא לאפשר שינוי של המפה המקורית מבחוץ
        int[][] copy = new int[getWidth()][getHeight()];
        for (int x = 0; x < getWidth(); x++) {
            System.arraycopy(_map[x], 0, copy[x], 0, getHeight());
        }
        return copy;
    }

    @Override public int getWidth() { return _map.length; }
    @Override public int getHeight() { return _map[0].length; }
    @Override public int getPixel(int x, int y) { return _map[x][y]; }
    @Override public int getPixel(Pixel2D p) { return _map[p.getX()][p.getY()]; }

    @Override
    public void setPixel(int x, int y, int v) { _map[x][y] = v; }
    @Override
    public void setPixel(Pixel2D p, int v) { _map[p.getX()][p.getY()] = v; }

    @Override public void setCyclic(boolean c) { _cyclic = c; }
    @Override public boolean isCyclic() { return _cyclic; }
    @Override public boolean isInside(Pixel2D p) {
        return p != null && p.getX() >= 0 && p.getX() < getWidth() && p.getY() >= 0 && p.getY() < getHeight();
    }

    // מימוש ה-BFS (נשאר כפי שהיה, הוא תקין)
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        int[][] dists = new int[getWidth()][getHeight()];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) dists[i][j] = -1;
        }
        Queue<Pixel2D> q = new LinkedList<>();
        dists[start.getX()][start.getY()] = 0;
        q.add(start);
        int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();
            int d = dists[curr.getX()][curr.getY()];
            for (int i = 0; i < 4; i++) {
                int nx = curr.getX() + dx[i];
                int ny = curr.getY() + dy[i];
                if (_cyclic) {
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }
                if (nx >= 0 && nx < getWidth() && ny >= 0 && ny < getHeight()) {
                    if (_map[nx][ny] != obsColor && dists[nx][ny] == -1) {
                        dists[nx][ny] = d + 1;
                        q.add(new Index2D(nx, ny));
                    }
                }
            }
        }
        return new Map(dists);
    }

    // פונקציית fill (נדרשת ב-Interface)
    @Override
    public int fill(Pixel2D p, int new_v) {
        // מימוש פשוט של צביעת שטח (Flood Fill)
        int old_v = getPixel(p);
        if (old_v == new_v) return 0;
        return fillRecursive(p.getX(), p.getY(), old_v, new_v);
    }

    private int fillRecursive(int x, int y, int old_v, int new_v) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight() || _map[x][y] != old_v) return 0;
        _map[x][y] = new_v;
        return 1 + fillRecursive(x+1, y, old_v, new_v) + fillRecursive(x-1, y, old_v, new_v) +
                fillRecursive(x, y+1, old_v, new_v) + fillRecursive(x, y-1, old_v, new_v);
    }

    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        // אפשר לממש זאת בעזרת allDistance אם תרצי, כרגע נחזיר null למימוש בסיסי
        return null;
    }
}