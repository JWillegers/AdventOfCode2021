package Day19;

import java.util.ArrayList;
import java.util.List;

public class Scanner{
    protected boolean matched;
    protected Cord cord;
    protected List<Beacon> beacons;

    public Scanner() {
        this.matched = false;
        this.beacons = new ArrayList<>();
        this.cord = new Cord();
    }

    public void calculateRelativePositions() {
        for(int i = 0; i < beacons.size(); i++) {
            Beacon b = beacons.get(i);
            int bx = b.cord.x;
            int by = b.cord.y;
            int bz = b.cord.z;
            for(int j = 0; j <= i; j++) {
                if (j == i) {
                    b.relativePositionsOtherBeacons.add(new Cord(0, 0, 0));
                } else {
                    Cord c = beacons.get(j).cord;
                    b.relativePositionsOtherBeacons.add(new Cord(Math.abs(c.x-bx), Math.abs(c.y-by), Math.abs(c.z-bz)));
                }
            }
        }
    }
}
