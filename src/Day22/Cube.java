package Day22;

public class Cube {
    boolean on;
    protected Cord min;
    protected Cord max;

    public Cube(int minx, int miny, int minz, int maxx, int maxy, int maxz, boolean on) {
        this.min = new Cord(minx, miny, minz);
        this.max = new Cord(maxx, maxy, maxz);
        this.on = on;
    }
}
