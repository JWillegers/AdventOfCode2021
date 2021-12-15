package Day15;

public class Path {
    protected int risk;
    protected boolean[][] visited;
    protected int x;
    protected int y;

    public Path(int x, int y, int risk, boolean[][] visited) {
        this.x = x;
        this.y = y;
        this.risk = risk;
        this.visited = visited;
    }

    public Path() {}
}
