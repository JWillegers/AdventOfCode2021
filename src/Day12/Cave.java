package Day12;

import java.util.ArrayList;
import java.util.List;

public class Cave {
    protected boolean big;
    protected String name;
    protected List<String> neighbours;

    public Cave(String name, boolean big, List<String> neighbours) {
        this.neighbours = new ArrayList<>();
        this.neighbours = neighbours;
        this.name = name;
        this.big = big;
    }
}
