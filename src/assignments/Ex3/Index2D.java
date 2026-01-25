package assignments.Ex3;

public class Index2D implements Pixel2D {
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}

    @Override
    public int getX() { return _x; }

    @Override
    public int getY() { return _y; }

    @Override
    public double distance2D(Pixel2D t) {
        if (t == null) { throw new RuntimeException("Pixel cannot be null"); }
        // חישוב מרחק אוקלידי: שורש של (הפרש איקס בריבוע + הפרש וואי בריבוע)
        double dx = this.getX() - t.getX();
        double dy = this.getY() - t.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return getX() + "," + getY();
    }

    @Override
    public boolean equals(Object t) {
        if (t instanceof Pixel2D) {
            Pixel2D p = (Pixel2D) t;
            // שימוש בפונקציית המרחק שמימשנו למעלה
            return (this.distance2D(p) == 0);
        }
        return false;
    }
}